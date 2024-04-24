const localhost: string = "http://localhost:8080";

/**
 * Function to turn email and name input into user object to be passed to backend, matching User model in backend
 * @param email - email associated with Google account
 * @param name - name associated with Google account
 */
function makeUser(email: string, name: string) {
    return {
        userUID: 1,
        userName: name,
        email: email,
    };
}

/**
 * Function to get the game records from the server.
 * @param email - email associated with Google account
 * @param name - name associated with Google account
 * @throws Error if the response is not valid
 */
export async function getGameRecords(email: any, name: any) {
    const url = `${localhost}/user/games`;
    const user = makeUser(email, name);

    try {
        const res = await fetch(url, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.parse(JSON.stringify(user)),
        });
        // checks that the response is valid
        if (!res.ok) {
            throw new Error("Failed to get records");
        }
        // creates and maps an array of Test Objects
        return await res.json();

    } catch (error) {
        console.log(error);
    }
}

export async function userHandler(email: any, name: any, method: string) {
  let url = "";
  if(method === "save") { url = `${localhost}/user/save`; }
  else if(method === "games") { url = `${localhost}/user/games`; }

  const user = makeUser(email, name);
  console.log(user);
  let headers = new Headers();
  headers.append('Content-Type', 'application/json');
  headers.append('Accept', 'application/json');
  headers.append('Origin','http://localhost:3000');

  try {
    const res = await fetch(url, {
      method: "POST",
      headers: headers,
      body: JSON.stringify(user),
    });
    // checks that the response is valid
    if (!res.ok) {
      throw new Error("Failed to save Player");
    }
    if(method === "games") {
      return await res.json();
    }

  } catch (error) {
    console.log(error);
  }
}

