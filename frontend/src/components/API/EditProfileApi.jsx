const API_BASE_URL = "http://localhost:8080/account";

export async function fetchAdminInfo(accountId) {
    const url = `${API_BASE_URL}/admininfo/${accountId}`;
    try {
        const response = await fetch(url);
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
        const response = await fetch(url);
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
        const response = await fetch(url);
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

