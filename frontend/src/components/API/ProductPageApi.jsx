export async function getProductInfo(id) {
    try {
        const response = await fetch("http://localhost:8080/productpage/" + id, {
            headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}` }
        });
        const data = await response.json();
        return data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}
  
export async function getComments(productId, queryParams) {
    try {
        const response = await fetch(`http://localhost:8080/productpage/comments/${productId}?${queryParams}`, {
            headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}` }
        });
        const data = await response.json();
        return data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export async function getImages(productId) {
    try {
        const response = await fetch(`http://localhost:8080/productpage/images/${productId}`, {
            headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}` }
        });
        if (!response.ok) {
          throw new Error("Failed to fetch images");
        }
        const data = await response.json();
        
        return data.map((imageDTO) => `data:image/jpeg;base64,${imageDTO.imageBytes}`);
      } catch (error) {
        console.error("Error fetching images:", error);
      }
}

