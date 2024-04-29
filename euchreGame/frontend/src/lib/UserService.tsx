import {gameRecord} from "@/lib/Types";

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
 * Function to handle user data, either saving user to db or retrieving user's saved games
 * @param email - user's email associated with Google account
 * @param name - user's name associated with Google account
 * @param method - either "save" to save user to db or "games" to retrieve user's saved games
 */
export async function userHandler(email: any, name: any, method: string) {
  let url = "";
  if(method === "save") { url = `${localhost}/user/save`; }
  else if(method === "games") { url = `${localhost}/user/games/reports`; }

  const user = makeUser(email, name);
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
      const temp: gameRecord[] = await res.json();
      console.log(temp);
      return temp;
    }

  } catch (error) {
    console.log(error);
  }
}

