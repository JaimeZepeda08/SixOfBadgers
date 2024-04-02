"use server";

const localhost: string = "backend";

export async function getPlayerHand() {
  const url = `http://${localhost}:8080/getHand`;
  try {
    const res = await fetch(url, {
      method: "GET",
    });
    if (!res.ok) {
      throw new Error("Failed to player hands");
    }
    return await res.text();
  } catch (error) {
    console.log(error);
  }
}
