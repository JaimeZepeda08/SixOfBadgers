const localhost: string = "backend";
export async function getHello() {
    const url = `http://${localhost}:8080/sayhello`;
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
export async function getGameRecords() {
    const url = `http://${localhost}:8080/getGameRecords`;
    try {
        const res = await fetch(url, {
            method: 'GET'
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

export async function createUser(formData: FormData) {
    "use server"
    const url = `http://${localhost}:8080/player/save`;
    const user = {
        userName: formData.get("userName"),
        password: formData.get("password")
    }
    console.log(user)
    try {
        const res = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(user)
        });
        // checks that the response is valid
        if (!res.ok) {
            throw new Error("Failed to save Player");
        }
        return res.text();
    } catch (error) {
        console.log(error);
    }
}

export async function currentSelectedCard(formData: FormData) {
    "use server"
    const url = `http://${localhost}:8080/selected_card`;
    // const card = {card: formData.get("card")}
    // console.log(card);
    try {
        const res = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            // body: JSON.stringify(card);
        });
        if (!res.ok) {
            throw new Error("Failed to save selected card")
        } 
        return res.text();
    } catch (error) {
        console.log(error);
    }
}

export async function getUser(formData: FormData) {
    "use server"
    const url = `http://${localhost}:8080/player/get`;
    const user = {
        userName: formData.get("userName"),
        password: formData.get("password")
    }
    console.log(user)
    try {
        const res = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(user)
        });
        // checks that the response is valid
        if (!res.ok) {
            throw new Error("Failed to get Player");
        }
        return res.json();
    } catch (error) {
        console.log(error);
    }
}
