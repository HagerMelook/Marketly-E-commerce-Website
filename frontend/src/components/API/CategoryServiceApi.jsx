import axios from 'axios';

const BASE_URL = 'http://localhost:8080/categories';

// Create axios instance with base configuration
const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

export const CategoryService = {
  // Get all categories
  getAllCategories: async () => {
    try {
      const response = await api.get('');
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch categories: ' + error.message);
    }
  },

  // Get single category by name
  getCategory: async (categoryName) => {
    try {
      const response = await api.get(`/${categoryName}`);
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch category: ' + error.message);
    }
  },

  // Add new category
  addCategory: async (categoryName, categoryImage) => {
    try {
      const formData = new FormData();
      formData.append('name', categoryName);
      formData.append('image', categoryImage);

      const response = await api.post('', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      return response.data;
    } catch (error) {
      throw new Error('Failed to add category: ' + error.message);
    }
  },

  updateCategory:async (categoryName, categoryDTO) => {
    try {
      const formData = new FormData();
      formData.append('newName', categoryDTO.categoryName);
      
      // Append image only if it's provided
      if (categoryDTO.images) {
        formData.append('image', categoryDTO.images);
      }
  
      const response = await api.put(`/${categoryName}`, formData);
      return response.data;
    } catch (error) {
      throw new Error('Failed to update category: ' + error.message);
    }
  },
  

  // Delete category
  deleteCategory: async (categoryName) => {
    try {
      await api.delete(`/${categoryName}`);
      return true;
    } catch (error) {
      throw new Error('Failed to delete category: ' + error.message);
    }
  }
};

export default CategoryService;