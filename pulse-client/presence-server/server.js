// Tiny presence server for Lone Client. No dependencies: `node server.js` (PORT env, default 8787).
// POST /hello  {"uuid","name","version"}  -> marks that player as a Lone Client user for 90s
// GET  /online                            -> ["uuid", ...]
const http = require("http");
const PORT = process.env.PORT || 8787;
const TTL = 90 * 1000;
const UUID = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;
const seen = new Map();

setInterval(() => {
  const now = Date.now();
  for (const [id, t] of seen) if (now - t > TTL) seen.delete(id);
}, 15000);

http.createServer((req, res) => {
  if (req.method === "GET" && (req.url === "/" || req.url === "/healthz")) {
    res.writeHead(200, { "Content-Type": "text/plain" });
    return res.end("Lone presence server ok");
  }
  if (req.method === "GET" && req.url === "/online") {
    res.writeHead(200, { "Content-Type": "application/json" });
    return res.end(JSON.stringify([...seen.keys()]));
  }
  if (req.method === "POST" && req.url === "/hello") {
    let body = "";
    req.on("data", (c) => { body += c; if (body.length > 2048) req.destroy(); });
    req.on("end", () => {
      try {
        const id = String(JSON.parse(body).uuid || "").toLowerCase();
        if (UUID.test(id)) seen.set(id, Date.now());
        res.writeHead(204); res.end();
      } catch { res.writeHead(400); res.end(); }
    });
    return;
  }
  res.writeHead(404); res.end();
}).listen(PORT, () => console.log("Lone presence server on :" + PORT));
