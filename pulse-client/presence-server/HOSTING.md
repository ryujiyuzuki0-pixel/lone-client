# Free hosting on Render (about 5 minutes)

1. Make a free GitHub account, create a new repo called lone-presence, and upload the files in this folder
   (server.js, package.json, render.yaml) using "Add file > Upload files".
2. Make a free account at render.com and log in with GitHub.
3. New > Web Service > pick the lone-presence repo.
   Runtime: Node. Build command: leave empty or `echo ok`. Start command: `node server.js`. Instance type: Free.
4. Click Create Web Service. After a minute Render shows your address, like https://lone-presence.onrender.com
5. Open that address in a browser. You should see "Lone presence server ok".
6. Put the address in SERVER_URL in presence/Presence.java and build the client.

Notes: the free plan sleeps after 15 minutes with no traffic and takes about a minute to wake up. Clients ping it
every 20 seconds, so it stays awake while anyone is playing. The list is kept in memory, which is fine because
clients re-announce themselves every 20 seconds.
