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
              errorDetails = await response.json(); // Try JSON first
          } catch {
              try {
                  errorDetails = await response.text(); // Then try text
              } catch {
                  errorDetails = `HTTP error! status: ${response.status}`;
              }
          }
          throw new Error(`Authentication failed: ${errorDetails}`);
      }

      // Handle the case where the response is ONLY the JWT as plain text
      const token = await response.text();
      console.log("JWT Token received:", token);
      return { token }; // Return an object with the token property
  } catch (error) {
      console.error("Sign-in error:", error);
      throw error;
  }
}

export default BasicSignIn;