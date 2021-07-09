// tag:app:start
// Has to be called first for instrumentation to work!
require("./instrumentation");

const express = require("express");
const axios = require("axios");

const app = express();

app.get("/db-status", (_req, res) => {
  res.send("database ok");
});

app.get("/api-status", (_req, res) => {
  res.send("api ok");
});

app.get("/status", async (_req, res) => {
  const [db, api] = await Promise.all([
    axios
      .get("http://localhost:8080/db-status")
      .then((result) => result.data)
      .catch(() => "db not responding"),
    axios
      .get("http://localhost:8080/api-status")
      .then((result) => result.data)
      .catch(() => "api not responding"),
  ]);

  res.send(`(${db}, ${api})`);
});

app.listen(8080, () => {
  console.log("server listening on port 8080");
});
// tag:app:end
