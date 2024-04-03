"use server";

// Setting up the constant variable "localhost" to store the server address in the docker container
const localhost: string = "backend";

/**
 * This function asynchronously fetches the player's hand from the backend server
 *
 * @returns a text representation of the player's hand
 *
 * @author jaime zepeda
 */
export async function getPlayerHand() {
  const url = `http://${localhost}:8080/getHand`; // construct URL
  try {
    // fetch data from backend
    const res = await fetch(url, {
      method: "GET",
    });
    // check response status (expected: 200)
    if (!res.ok) {
      throw new Error("Failed to player hands");
    }
    return await res.text();
  } catch (error) {
    console.log(error);
  }
}

export async function getOpponents() {
  const url = `http://${localhost}:8080/getOpponents`; // construct URL
  try {
    // fetch data from backend
    const res = await fetch(url, {
      method: "GET",
    });
    // check response status (expected: 200)
    if (!res.ok) {
      throw new Error("Failed to opponents");
    }
    return await res.text();
  } catch (error) {
    console.log(error);
  }
}
