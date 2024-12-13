import axios from "axios";
import { API_QUERY_LATEST_POSTS_URL, API_QUERY_MOST_LIKED_POSTS_URL, API_QUERY_MOST_VIEWED_POSTS_URL, API_QUERY_POST_DETAIL_URL, API_QUERY_SEARCH_POSTS_BY_CONTENT_URL, API_QUERY_SEARCH_POSTS_BY_TEXT_URL, API_QUERY_SEARCH_POSTS_BY_TITLE_URL } from "../util/apiUrl";

// 전체 게시글 최신순으로 가져오기
export const getLatestPosts = async (page: number = 0, size: number = 40) => {
    try {
        const response = await axios.get(API_QUERY_LATEST_POSTS_URL(), {
            params: { page, size },
        });
        console.log("Latest posts fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching latest posts:", error);
        throw error;
    }
};

// 전체 게시글 좋아요순으로 가져오기
export const getMostLikedPosts = async (page: number = 0, size: number = 40) => {
    try {
        const response = await axios.get(API_QUERY_MOST_LIKED_POSTS_URL(), {
            params: { page, size },
        });
        console.log("Most liked posts fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching most liked posts:", error);
        throw error;
    }
};

// 전체 게시글 조회수순으로 가져오기
export const getMostViewedPosts = async (page: number = 0, size: number = 40) => {
    try {
        const response = await axios.get(API_QUERY_MOST_VIEWED_POSTS_URL(), {
            params: { page, size },
        });
        console.log("Most viewed posts fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching most viewed posts:", error);
        throw error;
    }
};

// 제목으로 게시글 검색하기
export const searchPostsByTitle = async (title: string, page: number = 0, size: number = 40) => {
    try {
        const response = await axios.get(API_QUERY_SEARCH_POSTS_BY_TITLE_URL(), {
            params: { title, page, size },
        });
        console.log("Posts searched by title successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error searching posts by title:", error);
        throw error;
    }
};

// 내용으로 게시글 검색하기
export const searchPostsByContent = async (content: string, page: number = 0, size: number = 40) => {
    try {
        const response = await axios.get(API_QUERY_SEARCH_POSTS_BY_CONTENT_URL(), {
            params: { content, page, size },
        });
        console.log("Posts searched by content successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error searching posts by content:", error);
        throw error;
    }
};

// 텍스트로 게시글 검색하기
export const searchPostsByText = async (text: string, page: number = 0, size: number = 40) => {
    try {
        const response = await axios.get(API_QUERY_SEARCH_POSTS_BY_TEXT_URL(), {
            params: { text, page, size },
        });
        console.log("Posts searched by text successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error searching posts by text:", error);
        throw error;
    }
};

// 게시글 상세 정보 가져오기
export const getPostDetail = async (postUuid: string) => {
    try {
        const response = await axios.get(API_QUERY_POST_DETAIL_URL(postUuid));
        console.log("Post detail fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching post detail:", error);
        throw error;
    }
};