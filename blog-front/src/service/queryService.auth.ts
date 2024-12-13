import axios from "axios";
import { API_QUERY_USER_COMMENTS_URL, API_QUERY_USER_LATEST_POSTS_URL, API_QUERY_USER_LIKED_POSTS_URL, API_QUERY_USER_MOST_LIKED_POSTS_URL, API_QUERY_USER_MOST_VIEWED_POSTS_URL, API_QUERY_USER_POSTS_URL, API_QUERY_USER_PROFILE_URL, API_QUERY_USER_SEARCH_POSTS_BY_CONTENT_URL, API_QUERY_USER_SEARCH_POSTS_BY_TEXT_URL, API_QUERY_USER_SEARCH_POSTS_BY_TITLE_URL } from "../util/apiUrl";

// 사용자가 좋아요 누른 게시글 목록 가져오기
export const getUserLikedPosts = async (userUuid: string) => {
    try {
        const response = await axios.get(API_QUERY_USER_LIKED_POSTS_URL(userUuid));
        console.log("User's liked posts fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching user's liked posts:", error);
        throw error;
    }
};

// 사용자가 작성한 댓글 목록 가져오기
export const getUserComments = async (userUuid: string) => {
    try {
        const response = await axios.get(API_QUERY_USER_COMMENTS_URL(userUuid));
        console.log("User's comments fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching user's comments:", error);
        throw error;
    }
};

// 사용자가 작성한 게시글 목록 가져오기
export const getUserPosts = async (userUuid: string) => {
    try {
        const response = await axios.get(API_QUERY_USER_POSTS_URL(userUuid));
        console.log("User's posts fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching user's posts:", error);
        throw error;
    }
};

// 사용자가 작성한 게시글 최신순으로 가져오기
export const getUserLatestPosts = async (userUuid: string, page: number = 0, size: number = 20) => {
    try {
        const response = await axios.get(API_QUERY_USER_LATEST_POSTS_URL(userUuid), {
            params: { page, size },
        });
        console.log("User's latest posts fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching user's latest posts:", error);
        throw error;
    }
};

// 사용자가 작성한 게시글 좋아요순으로 가져오기
export const getUserMostLikedPosts = async (userUuid: string, page: number = 0, size: number = 20) => {
    try {
        const response = await axios.get(API_QUERY_USER_MOST_LIKED_POSTS_URL(userUuid), {
            params: { page, size },
        });
        console.log("User's most liked posts fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching user's most liked posts:", error);
        throw error;
    }
};

// 사용자가 작성한 게시글 조회수순으로 가져오기
export const getUserMostViewedPosts = async (userUuid: string, page: number = 0, size: number = 20) => {
    try {
        const response = await axios.get(API_QUERY_USER_MOST_VIEWED_POSTS_URL(userUuid), {
            params: { page, size },
        });
        console.log("User's most viewed posts fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching user's most viewed posts:", error);
        throw error;
    }
};

// 사용자 프로필 정보 가져오기
export const getUserProfile = async (userUuid: string) => {
    try {
        const response = await axios.get(API_QUERY_USER_PROFILE_URL(userUuid));
        console.log("User profile fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching user profile:", error);
        throw error;
    }
};

// 제목으로 사용자 게시글 검색
export const searchUserPostsByTitle = async (
    userUuid: string,
    title: string,
    page: number = 0,
    size: number = 20
) => {
    try {
        const response = await axios.get(API_QUERY_USER_SEARCH_POSTS_BY_TITLE_URL(userUuid), {
            params: { title, page, size },
        });
        console.log("User's posts searched by title successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error searching user's posts by title:", error);
        throw error;
    }
};

// 내용으로 사용자 게시글 검색
export const searchUserPostsByContent = async (
    userUuid: string,
    content: string,
    page: number = 0,
    size: number = 20
) => {
    try {
        const response = await axios.get(API_QUERY_USER_SEARCH_POSTS_BY_CONTENT_URL(userUuid), {
            params: { content, page, size },
        });
        console.log("User's posts searched by content successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error searching user's posts by content:", error);
        throw error;
    }
};

// 텍스트로 사용자 게시글 검색
export const searchUserPostsByText = async (
    userUuid: string,
    text: string,
    page: number = 0,
    size: number = 20
) => {
    try {
        const response = await axios.get(API_QUERY_USER_SEARCH_POSTS_BY_TEXT_URL(userUuid), {
            params: { text, page, size },
        });
        console.log("User's posts searched by text successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error searching user's posts by text:", error);
        throw error;
    }
};