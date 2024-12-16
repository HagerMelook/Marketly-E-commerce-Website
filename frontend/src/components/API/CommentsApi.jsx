const API_BASE_URL = "http://localhost:8080/accountcomments";

export async function getComments(accountId, queryParams) {
  try {
    const response = await fetch(`${API_BASE_URL}/${accountId}?${queryParams}`);
    if (!response.ok) {
      throw new Error("Failed to fetch comments");
    }
    return await response.json();
  } catch (error) {
    console.error("Error fetching comments:", error);
    throw error;
  }
}

export async function createComment(commentDTO) {
  try {
    const response = await fetch(API_BASE_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(commentDTO),
    });
    if (!response.ok) {
      throw new Error("Failed to create comment");
    }
  } catch (error) {
    console.error("Error creating comment:", error);
    throw error;
  }
}


export async function updateComment(commentId, commentDTO) {
  try {
    const response = await fetch(`${API_BASE_URL}/${commentId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(commentDTO),
    });
    if (!response.ok) {
      if (response.status === 404) {
        throw new Error("Comment not found");
      }
      throw new Error("Failed to update comment");
    }
  } catch (error) {
    console.error("Error updating comment:", error);
    throw error;
  }
}

export async function deleteComment(commentId) {
  try {
    const response = await fetch(`${API_BASE_URL}/${commentId}`, {
      method: "DELETE",
    });
    if (!response.ok) {
      throw new Error("Failed to delete comment");
    }
  } catch (error) {
    console.error("Error deleting comment:", error);
    throw error;
  }
}
