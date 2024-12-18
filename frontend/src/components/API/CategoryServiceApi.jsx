const API_BASE_URL = "http://localhost:8080/categories";

export function fetchCategories() {
  return fetch(`${API_BASE_URL}`)
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed to fetch categories");
      }
      return response.json();
    })
    .catch((error) => {
      console.error("Error fetching categories:", error);
      throw error;
    });
}

export function addCategory(category) {
  return fetch(`${API_BASE_URL}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(category),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed to add category");
      }
      return response.json();
    })
    .catch((error) => {
      console.error("Error adding category:", error);
      throw error;
    });
}

export function updateCategory(categoryName, updatedCategory) {
  return fetch(`${API_BASE_URL}/${categoryName}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(updatedCategory),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed to update category");
      }
      return response.json();
    })
    .catch((error) => {
      console.error("Error updating category:", error);
      throw error;
    });
}

export function deleteCategory(categoryName) {
  return fetch(`${API_BASE_URL}/${categoryName}`, {
    method: "DELETE",
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed to delete category");
      }
    })
    .catch((error) => {
      console.error("Error deleting category:", error);
      throw error;
    });
}
