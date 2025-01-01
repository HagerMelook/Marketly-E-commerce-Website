const API_BASE_URL = 'http://localhost:8080';

export const fetchVendorOrders = async (page, pageSize, sortDir, status) => {
  const url = `${API_BASE_URL}/vendororders?accountId=${sessionStorage.getItem('id')}&page=${page}&size=${pageSize}&sortDir=${sortDir}${status !== '' ? `&status=${status}` : ''}`;
  const response = await fetch(url, {
        headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}` }
    });
  if (!response.ok) throw new Error('Failed to fetch vendor orders');
  return response.json();
};

export const fetchClientInfo = async (accountId) => {
  const url = `${API_BASE_URL}/account/clientinfo/${accountId}`;
  const response = await fetch(url, {
        headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}` }
    });
  if (!response.ok) throw new Error('Failed to fetch client info');
  return response.json();
};
