
export async function getHello() {
    let backend = "backend";
    const url = `http://${backend}:8080/sayhello`;
    try {
        const res = await fetch(url, {
            cache: "no-store",
        });
        // checks that the response is valid
        if (!res.ok) {
            throw new Error("Failed to get tests");
        }
        // creates and maps an array of Test Objects
        return await res.text();

    } catch (error) {
        console.log(error);
    }
}