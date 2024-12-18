const API_BASE_URL = "http://localhost:8080/accountrates";

export async function getRates(accountId, queryParams) {
  try {
    const response = await fetch(`${API_BASE_URL}/${accountId}?${queryParams}`);
    if (!response.ok) {
      throw new Error("Failed to fetch rates");
    }
    return await response.json();
  } catch (error) {
    console.error("Error fetching rates:", error);
    throw error;
  }
}

export async function getRateByProduct(accountId, productId) {
  try {
    const response = await fetch(`${API_BASE_URL}/product/${productId}/${accountId}`);
    if (!response.ok) {
      if (response.status === 404) {
        return null;
      }
    }
    return await response.json();
  } catch (error) {
    console.log("No rate found"+error);
  }
}

export async function createRate(rateDTO) {
  try {
    const response = await fetch(API_BASE_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(rateDTO),
    });
    if (!response.ok) {
      throw new Error("Failed to create rate");
    }
  } catch (error) {
    console.error("Error creating rate:", error);
    throw error;
  }
}


export async function updateRate(rateId, rateDTO) {
  try {
    const response = await fetch(`${API_BASE_URL}/${rateId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(rateDTO),
    });
    if (!response.ok) {
      if (response.status === 404) {
        throw new Error("rate not found");
      }
      throw new Error("Failed to update rate");
    }
  } catch (error) {
    console.error("Error updating rate:", error);
    throw error;
  }
}

export async function deleteRate(rateId) {
  try {
    const response = await fetch(`${API_BASE_URL}/${rateId}`, {
      method: "DELETE",
    });
    if (!response.ok) {
      throw new Error("Failed to delete rate");
    }
  } catch (error) {
    console.error("Error deleting rate:", error);
    throw error;
  }
}
