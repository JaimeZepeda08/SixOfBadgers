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

/**
 * Function gives played card from user to the controller 
 * json inclused suit and value of card
 * 
 * @param formData json of card data
 * @returns confirmation from backend or throws an error if failed to do so
 */
export async function submitSelectedCard(formData: FormData) {
  const url = `http://${localhost}:8080/selectedCard`;
  const card = {
      suite: formData.get("suit"),
      value: formData.get("value")
  }
  console.log(card)
  try {
      const res = await fetch(url, {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json',
          },
          body: JSON.stringify(card)
      });
      if (!res.ok) {
          throw new Error("Failed to save selected card")
      } 
      return res.text();
  } catch (error) {
      console.log(error);
  }
}


export async function getPlayers() {
  const url = `http://${localhost}:8080/selectedCard`;
  const players = {

  }
  console.log(players)
  try {
      const res = await fetch(url, {
          method: 'GET',
          headers: {
              'Content-Type': 'application/json',
          },
          body: JSON.stringify(players)
      });
      if (!res.ok) {
          throw new Error("Failed to get players")
      } 
      return res.text();
  } catch (error) {
      console.log(error);
  }
}

