import jwtDecode from 'jwt-decode';

function decodeJwt(token) {
  try {
    const decoded = jwtDecode(token);
    return decoded;
  } catch (error) {
    console.error("Error decoding JWT:", error);
    return null;
  }
}
export default decodeJwt;