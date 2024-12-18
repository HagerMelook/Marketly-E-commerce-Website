import { useState, useEffect } from "react";
import {
  fetchCategories,
  addCategory,
  updateCategory,
  deleteCategory,
} from "../../components/API/CategoryServiceApi";

const CategoryManagement = () => {
  const [categories, setCategories] = useState([]);
  const [newCategory, setNewCategory] = useState({
    categoryName: "",
    categoryImagePath: "",
  });
  const [editCategory, setEditCategory] = useState(null);

  // Fetch categories on component mount
  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    try {
      const data = await fetchCategories();
      setCategories(data);
    } catch (error) {
      console.error("Error loading categories:", error);
    }
  };

  const handleAddCategory = async () => {
    try {
      await addCategory(newCategory);
      setNewCategory({ categoryName: "", categoryImagePath: "" });
      loadCategories();
    } catch (error) {
      console.error("Error adding category:", error);
    }
  };

  const handleUpdateCategory = async () => {
    try {
      await updateCategory(editCategory.categoryName, editCategory);
      setEditCategory(null);
      loadCategories();
    } catch (error) {
      console.error("Error updating category:", error);
    }
  };

  const handleDeleteCategory = async (categoryName) => {
    try {
      await deleteCategory(categoryName);
      loadCategories();
    } catch (error) {
      console.error("Error deleting category:", error);
    }
  };

  const handleImageUpload = (e, setCategory) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        setCategory((prev) => ({
          ...prev,
          categoryImagePath: reader.result,
        }));
      };
      reader.readAsDataURL(file);
    }
  };

  return (
    <div className="container">
      <h1>Category Management</h1>

      {/* Add New Category */}
      <div className="add-category">
        <h2>Add Category</h2>
        <input
          type="text"
          placeholder="Category Name"
          value={newCategory.categoryName}
          onChange={(e) =>
            setNewCategory((prev) => ({ ...prev, categoryName: e.target.value }))
          }
        />
        <input
          type="file"
          onChange={(e) => handleImageUpload(e, setNewCategory)}
        />
        <button onClick={handleAddCategory}>Add Category</button>
      </div>

      {/* Existing Categories */}
      <div className="category-list">
        <h2>Existing Categories</h2>
        {categories.map((category) => (
          <div key={category.categoryName} className="category-item">
            {editCategory && editCategory.categoryName === category.categoryName ? (
              <div className="edit-category">
                <input
                  type="text"
                  value={editCategory.categoryName}
                  disabled
                />
                <input
                  type="file"
                  onChange={(e) => handleImageUpload(e, setEditCategory)}
                />
                <button onClick={handleUpdateCategory}>Save</button>
                <button onClick={() => setEditCategory(null)}>Cancel</button>
              </div>
            ) : (
              <div className="category-display">
                <img src={category.categoryImagePath} alt={category.categoryName} />
                <h3>{category.categoryName}</h3>
                <button onClick={() => setEditCategory(category)}>Edit</button>
                <button onClick={() => handleDeleteCategory(category.categoryName)}>
                  Delete
                </button>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default CategoryManagement;