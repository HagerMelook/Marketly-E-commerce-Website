const API_BASE_URL = "http://localhost:8080/account";

export async function fetchAdminInfo(accountId) {
    const url = `${API_BASE_URL}/admininfo/${accountId}`;
    try {
        const response = await fetch(url, {
            headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}` }
        });
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const adminInfo = await response.json();
        return adminInfo;
    } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
        return null;
    }
}

export async function fetchClientInfo(accountId) {
    const url = `${API_BASE_URL}/clientinfo/${accountId}`;
    try {
        const response = await fetch(url, {
            headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}` }
        });
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const adminInfo = await response.json();
        return adminInfo;
    } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
        return null;
    }
}

export async function fetchVendorInfo(accountId) {
    const url = `${API_BASE_URL}/vendorinfo/${accountId}`;
    try {
        const response = await fetch(url, {
            headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}` }
        });
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const adminInfo = await response.json();
        return adminInfo;
    } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
        return null;
    }
}

export async function updateAdminInfo(accountId, patch) {
    const url = `${API_BASE_URL}/admininfo/${accountId}`;
    
    try {
      const response = await fetch(url, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json-patch+json",
          'Authorization': `Bearer ${sessionStorage.getItem('token')}`
        },
        body: JSON.stringify(patch),
      });
  
      if (!response.ok) {
        if (response.status === 400) {
          throw new Error("Bad Request: Invalid patch data.");
        } else if (response.status === 404) {
          throw new Error("Not Found: Admin ID does not exist.");
        } else {
          throw new Error(`Error: Received status code ${response.status}`);
        }
      }

    } catch (error) {
      console.error("Failed to update admin info:", error);
      throw error;
    }
  }

  export async function updateVendorInfo(accountId, patch) {
    const url = `${API_BASE_URL}/vendorinfo/${accountId}`;
    
    try {
      const response = await fetch(url, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json-patch+json",
          'Authorization': `Bearer ${sessionStorage.getItem('token')}`
        },
        body: JSON.stringify(patch),
      });
  
      if (!response.ok) {
        if (response.status === 400) {
          throw new Error("Bad Request: Invalid patch data.");
        } else if (response.status === 404) {
          throw new Error("Not Found: Vendor ID does not exist.");
        } else {
          throw new Error(`Error: Received status code ${response.status}`);
        }
      }

    } catch (error) {
      console.error("Failed to update vendor info:", error);
      throw error;
    }
  }

  export async function updateClientInfo(accountId, patch) {
    const url = `${API_BASE_URL}/clientinfo/${accountId}`;
    
    try {
      const response = await fetch(url, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json-patch+json",
          'Authorization': `Bearer ${sessionStorage.getItem('token')}`
        },
        body: JSON.stringify(patch),
      });
  
      if (!response.ok) {
        if (response.status === 400) {
          throw new Error("Bad Request: Invalid patch data.");
        } else if (response.status === 404) {
          throw new Error("Not Found: Client ID does not exist.");
        } else {
          throw new Error(`Error: Received status code ${response.status}`);
        }
      }

    } catch (error) {
      console.error("Failed to update client info:", error);
      throw error;
    }
  }