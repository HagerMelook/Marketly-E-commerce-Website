export async function ClientBasicSignUp(username, password) {
  const url = `http://localhost:8080/SignUp/ClientBasicSignUp/${password}`;

  try {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username }),
    });

    if (!response.ok) {
      throw new Error(`HTTP Error ${response.status}: ${response.statusText}`);
    }

    const data = await response.json();
    return {
      success: true,
      data,
    };
  } catch (error) {
    console.error("Error during ClientBasicSignUp:", error.message);
    return {
      success: false,
      error: error.message,
    };
  }
}
export default ClientBasicSignUp;
