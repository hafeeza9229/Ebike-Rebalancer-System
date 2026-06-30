import express from "express";
import path from "path";
import { fileURLToPath } from "url";

const app = express();
app.use(express.json());

const PORT = 3000;

// Model States (In-Memory Database)
let stations = [
  { id: 1, name: "Central Hub", location: "Downtown Boulevard", currentBikes: 12, maxCapacity: 15 },
  { id: 2, name: "Saddar Metro", location: "Saddar Main Crossing", currentBikes: 2, maxCapacity: 20 },
  { id: 3, name: "Defense Chowk", location: "DHA Phase 2 Crossing", currentBikes: 8, maxCapacity: 14 },
  { id: 4, name: "Clifton Beach", location: "Clifton Marina Road", currentBikes: 1, maxCapacity: 12 },
  { id: 5, name: "Gulshan Ground", location: "Gulshan Block 4 Sector", currentBikes: 15, maxCapacity: 15 }
];

let vehicles = [
  { id: 101, type: "EBike", location: "Saddar Metro", batteryLevel: 85, status: "AVAILABLE", maintenanceCost: 1500 },
  { id: 102, type: "EBike", location: "Clifton Beach", batteryLevel: 14, status: "AVAILABLE", maintenanceCost: 3800 },
  { id: 103, type: "ManualBike", location: "Central Hub", batteryLevel: 100, status: "AVAILABLE", maintenanceCost: 0 },
  { id: 104, type: "EBike", location: "Defense Chowk", batteryLevel: 55, status: "IN_USE", maintenanceCost: 2000 },
  { id: 105, type: "EBike", location: "Gulshan Ground", batteryLevel: 92, status: "MAINTENANCE", maintenanceCost: 8500 }
];

let rebalanceHistory = [
  {
    id: "reb-001",
    timestamp: new Date(Date.now() - 36000000).toISOString(),
    routeTaken: "Gulshan Ground -> Saddar Metro",
    stationsServiced: 1,
    fuelSaved: 2.4
  }
];

let registeredUsers = [
  { email: "admin@ebike.com", name: "Admin", password: "admin123", role: "ADMIN" },
  { email: "user@ebike.com", name: "User", password: "user123", role: "USER" }
];

// AUTHENTICATION ENDPOINTS
app.post("/api/auth/register", (req, res) => {
  const { username, password } = req.body;
  if (!username || !password) {
    return res.status(400).json({ error: "Username and password are required" });
  }

  const exists = registeredUsers.some(u => u.email === username);
  if (exists) {
    return res.status(400).json({ error: "Username already exists" });
  }

  const newUser = {
    email: username,
    name: username.split("@")[0],
    password: password,
    role: (username === "admin@ebike.com" ? "ADMIN" : "USER")
  };

  registeredUsers.push(newUser);
  res.json({ message: "Registration successful" });
});

app.post("/api/auth/login", (req, res) => {
  const { username, password } = req.body;
  if (!username || !password) {
    return res.status(400).json({ error: "All fields are required" });
  }

  const user = registeredUsers.find(u => u.email === username && u.password === password);
  if (!user) {
    return res.status(401).json({ error: "Invalid credentials" });
  }

  res.json({
    email: user.email,
    name: user.name,
    role: user.role,
    loggedIn: true
  });
});

app.get("/api/user/me", (req, res) => {
  res.json({
    authenticated: true,
    email: "admin@ebike.com",
    username: "admin@ebike.com",
    name: "Admin",
    role: "ADMIN",
    loggedIn: true
  });
});

// STATION API ENDPOINTS
app.get("/api/stations", (req, res) => {
  res.json(stations);
});

app.get("/api/stations/needs-rebalance", (req, res) => {
  const critical = stations.filter(s => {
    const occ = s.maxCapacity ? (s.currentBikes / s.maxCapacity) : 0;
    return occ < 0.2;
  });
  res.json(critical);
});

app.get("/api/stations/:id", (req, res) => {
  const id = Number(req.params.id);
  const station = stations.find(s => s.id === id);
  if (!station) {
    return res.status(404).json({ error: "Station not found" });
  }
  res.json(station);
});

app.put("/api/stations/:id", (req, res) => {
  const id = Number(req.params.id);
  const stationIndex = stations.findIndex(s => s.id === id);
  if (stationIndex === -1) {
    return res.status(404).json({ error: "Station not found" });
  }
  const { name, location, currentBikes, maxCapacity } = req.body;
  const updatedStation = {
    ...stations[stationIndex],
    ...(name !== undefined && { name }),
    ...(location !== undefined && { location }),
    ...(currentBikes !== undefined && { currentBikes: Number(currentBikes) }),
    ...(maxCapacity !== undefined && { maxCapacity: Number(maxCapacity) })
  };
  stations[stationIndex] = updatedStation;
  res.json(updatedStation);
});

app.post("/api/stations", (req, res) => {
  const { name, location, currentBikes, maxCapacity } = req.body;
  if (!name || !location) {
    return res.status(400).json({ error: "Name and location are required" });
  }

  const newStation = {
    id: Date.now(),
    name,
    location,
    currentBikes: Number(currentBikes) || 0,
    maxCapacity: Number(maxCapacity) || 12
  };

  stations.push(newStation);
  res.status(201).json(newStation);
});

app.delete("/api/stations/:id", (req, res) => {
  const id = Number(req.params.id);
  const index = stations.findIndex(s => s.id === id);
  if (index !== -1) {
    stations.splice(index, 1);
  }
  res.json({ message: "Station deleted successfully" });
});

app.post("/api/stations/:id/rent", (req, res) => {
  const id = Number(req.params.id);
  const station = stations.find(s => s.id === id);

  if (!station) {
    return res.status(404).json({ error: "Station not found" });
  }

  if (station.currentBikes <= 0) {
    return res.status(400).json({ error: "No bikes available at this station" });
  }

  station.currentBikes -= 1;
  res.json(station);
});

app.post("/api/stations/:id/return", (req, res) => {
  const id = Number(req.params.id);
  const station = stations.find(s => s.id === id);

  if (!station) {
    return res.status(404).json({ error: "Station not found" });
  }

  if (station.currentBikes >= station.maxCapacity) {
    return res.status(400).json({ error: "Station is at maximum capacity" });
  }

  station.currentBikes += 1;
  res.json(station);
});

// VEHICLE API ENDPOINTS
app.get("/api/vehicles", (req, res) => {
  res.json(vehicles);
});

app.get("/api/vehicles/available", (req, res) => {
  const available = vehicles.filter(v => v.status === "AVAILABLE");
  res.json(available);
});

app.get("/api/vehicles/:id", (req, res) => {
  const id = Number(req.params.id);
  const vehicle = vehicles.find(v => v.id === id);
  if (!vehicle) {
    return res.status(404).json({ error: "Vehicle not found" });
  }
  res.json(vehicle);
});

app.delete("/api/vehicles/:id", (req, res) => {
  const id = Number(req.params.id);
  const index = vehicles.findIndex(v => v.id === id);
  if (index === -1) {
    return res.status(404).json({ error: "Vehicle not found" });
  }
  vehicles.splice(index, 1);
  res.send("Vehicle deleted");
});

app.get("/api/vehicles/:id/maintenance-cost", (req, res) => {
  const id = Number(req.params.id);
  const vehicle = vehicles.find(v => v.id === id);
  if (!vehicle) {
    return res.status(404).json({ error: "Vehicle not found" });
  }
  res.json(vehicle.maintenanceCost || 0);
});

app.post("/api/vehicles", (req, res) => {
  const { type, location, batteryLevel, status } = req.body;
  const newVehicle = {
    id: 100 + vehicles.length + 1,
    type: type || "EBike",
    location: location || "Central Hub",
    batteryLevel: batteryLevel !== undefined ? Number(batteryLevel) : 80,
    status: status || "AVAILABLE",
    maintenanceCost: status === "MAINTENANCE" ? Math.floor(Math.random() * 5000) + 1000 : 0
  };

  vehicles.push(newVehicle);
  res.status(201).json(newVehicle);
});

app.patch("/api/vehicles/:id/status", (req, res) => {
  const id = Number(req.params.id);
  const status = req.query.status;
  const location = req.query.location;
  const batteryLevel = req.query.batteryLevel;
  const vehicle = vehicles.find(v => v.id === id);

  if (!vehicle) {
    return res.status(404).json({ error: "Vehicle not found" });
  }

  if (status) {
    vehicle.status = status;
    if (status === "MAINTENANCE") {
      vehicle.maintenanceCost = (vehicle.maintenanceCost || 0) + Math.floor(Math.random() * 3000) + 500;
    }
  }

  if (location) {
    vehicle.location = location;
  }

  if (batteryLevel !== undefined && batteryLevel !== null) {
    vehicle.batteryLevel = Number(batteryLevel);
  }

  res.send(`Vehicle status updated to ${status}`);
});

// REBALANCING & ANALYTICS
app.get("/api/analytics/summary", (req, res) => {
  const totalStations = stations.length;
  const critical = stations.filter(s => {
    const occ = s.maxCapacity ? (s.currentBikes / s.maxCapacity) : 0;
    return occ < 0.2;
  });
  const healthyCount = totalStations - critical.length;

  const summary = {
    totalStations,
    healthyStations: healthyCount,
    stationsNeedingRebalance: critical.length,
    totalVehicles: vehicles.length,
    totalFuelSaved: rebalanceHistory.reduce((sum, item) => sum + item.fuelSaved, 0),
    rebalanceCount: rebalanceHistory.length
  };

  res.json(summary);
});

app.post("/api/rebalance/trigger", (req, res) => {
  const criticalStations = stations.filter(s => {
    const occ = s.maxCapacity ? (s.currentBikes / s.maxCapacity) : 0;
    return occ < 0.2;
  });

  if (criticalStations.length === 0) {
    return res.send("No stations currently require rebalancing. All operations are stable.");
  }

  const fullStations = stations.filter(s => {
    const occ = s.maxCapacity ? (s.currentBikes / s.maxCapacity) : 0;
    return occ >= 0.8;
  });

  let rebalancedCount = 0;
  let sourceStationNames = [];
  let destinationStationNames = [];

  criticalStations.forEach(crit => {
    const provider = fullStations.find(f => f.currentBikes > 5);
    if (provider) {
      provider.currentBikes -= 3;
      crit.currentBikes += 3;
      rebalancedCount++;
      if (!sourceStationNames.includes(provider.name)) sourceStationNames.push(provider.name);
      if (!destinationStationNames.includes(crit.name)) destinationStationNames.push(crit.name);
    } else {
      crit.currentBikes += 2;
      rebalancedCount++;
      if (!sourceStationNames.includes("Central Hub Depot")) sourceStationNames.push("Central Hub Depot");
      if (!destinationStationNames.includes(crit.name)) destinationStationNames.push(crit.name);
    }
  });

  const routeStr = `${sourceStationNames.join(", ")} ➔ ${destinationStationNames.join(", ")}`;
  const fuel = Number((rebalancedCount * 1.5).toFixed(1));

  const newLog = {
    id: `reb-${Math.floor(Date.now() / 100000).toString().slice(-4)}`,
    timestamp: new Date().toISOString(),
    routeTaken: routeStr,
    stationsServiced: criticalStations.length,
    fuelSaved: fuel
  };

  rebalanceHistory.push(newLog);

  res.send(`Successfully rebalanced ${criticalStations.length} critical stations. Serviced route: ${routeStr}. Saved approximately ${fuel}L of fuel.`);
});

app.get("/api/rebalance/history", (req, res) => {
  res.json(rebalanceHistory);
});

app.get("/api/rebalance/fuel-saved", (req, res) => {
  const fuel = rebalanceHistory.reduce((sum, item) => sum + item.fuelSaved, 0);
  res.json(fuel);
});

app.get("/api/export/stations/csv", (req, res) => {
  let csv = "Station ID,Station Name,Location,Current Bikes,Capacity,Occupancy Percentage,Status\n";
  stations.forEach(s => {
    const occ = s.maxCapacity ? Math.round((s.currentBikes / s.maxCapacity) * 100) : 0;
    const status = occ < 20 ? "CRITICAL/LOW" : "OK/STABLE";
    csv += `"${s.id}","${s.name}","${s.location}",${s.currentBikes},${s.maxCapacity},${occ}%,${status}\n`;
  });
  res.header("Content-Type", "text/csv");
  res.attachment("stations_export.csv");
  res.send(csv);
});

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Serve the static files from springboot-backend resources folder
const staticPath = path.join(__dirname, "static");
app.use(express.static(staticPath));

// Fallback all other client routing requests to index.html
app.get("*", (req, res) => {
  res.sendFile(path.join(staticPath, "index.html"));
});

// Launch server listener
app.listen(PORT, "0.0.0.0", () => {
  console.log(`Server successfully started at http://0.0.0.0:${PORT}`);
});
