async function BasicSignIn(username, password) {
    const basicAuth = btoa(`${username}:${password}`);

    try {
        const response = await fetch("http://localhost:8080/auth/signin", {
            method: "POST",
            headers: {
                "Authorization": `Basic ${basicAuth}`,
            },
        });

        if (!response.ok) {
          let errorDetails = null;
          try {
            errorDetails = await response.json(); // Try to parse JSON error response
          } catch {
            // If JSON parsing fails, use the text response
            errorDetails = await response.text();
          }
          if (response.status === 401) {
            throw new Error(`Authentication failed: Invalid credentials. ${errorDetails}`);
          } else if (response.status === 403) {
            throw new Error(`Authentication failed: Forbidden. ${errorDetails}`);
          } else {
              throw new Error(`Authentication failed with status: ${response.status}. ${errorDetails}`);
          }
        }

        const data = await response.json();
        console.log("JWT Token received:", data.token);
        return data;
    } catch (error) {
        console.error("Sign-in error:", error); // Log the full error object for debugging
        throw error; // Re-throw the error to be handled by the caller
    }
}

export default BasicSignIn;