import axios from "axios";
import { API_QUERY_NICKNAME_POSTS_URL, API_QUERY_USER_COMMENTED_POSTS_URL, API_QUERY_NICKNAME_LATEST_POSTS_URL, API_QUERY_USER_LIKED_POSTS_URL, API_QUERY_NICKNAME_MOST_LIKED_POSTS_URL, API_QUERY_NICKNAME_MOST_VIEWED_POSTS_URL, API_QUERY_NICKNAME_PROFILE_URL, API_QUERY_NICKNAME_SEARCH_POSTS_BY_CONTENT_URL, API_QUERY_NICKNAME_SEARCH_POSTS_BY_TEXT_URL, API_QUERY_NICKNAME_SEARCH_POSTS_BY_TITLE_URL } from "../util/apiUrl";

// 사용자가 좋아요 누른 게시글 목록 가져오기
export const getUserLikedPosts = async (userUuid: string, page: number = 0, size: number = 20) => {
    try {
        const response = await axios.get(API_QUERY_USER_LIKED_POSTS_URL(userUuid), {
            params: { page, size },
        });
        console.log("User's liked posts fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching user's liked posts:", error);
        throw error;
    }
};

// 사용자가 작성한 댓글 목록 가져오기
export const getUserCommentedPosts = async (userUuid: string, page: number = 0, size: number = 20) => {
    try {
        const response = await axios.get(API_QUERY_USER_COMMENTED_POSTS_URL(userUuid), {
            params: { page, size },
        });
        console.log("User's commented posts fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching user's commented posts:", error);
        throw error;
    }
};

// 사용자가 작성한 게시글 목록 가져오기
export const getNicknamePosts = async (nickname: string) => {
    try {
        const response = await axios.get(API_QUERY_NICKNAME_POSTS_URL(nickname));
        console.log("Nickname's posts fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching nickname's posts:", error);
        throw error;
    }
};

// 사용자가 작성한 게시글 최신순으로 가져오기
export const getNicknameLatestPosts = async (nickname: string, page: number = 0, size: number = 20) => {
    try {
        const response = await axios.get(API_QUERY_NICKNAME_LATEST_POSTS_URL(nickname), {
            params: { page, size },
        });
        console.log("Nickname's latest posts fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching nickname's latest posts:", error);
        throw error;
    }
};

// 사용자가 작성한 게시글 좋아요순으로 가져오기
export const getNicknameMostLikedPosts = async (nickname: string, page: number = 0, size: number = 20) => {
    try {
        const response = await axios.get(API_QUERY_NICKNAME_MOST_LIKED_POSTS_URL(nickname), {
            params: { page, size },
        });
        console.log("Nickname's most liked posts fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching nickname's most liked posts:", error);
        throw error;
    }
};

// 사용자가 작성한 게시글 조회수순으로 가져오기
export const getNicknameMostViewedPosts = async (nickname: string, page: number = 0, size: number = 20) => {
    try {
        const response = await axios.get(API_QUERY_NICKNAME_MOST_VIEWED_POSTS_URL(nickname), {
            params: { page, size },
        });
        console.log("Nickname's most viewed posts fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching nickname's most viewed posts:", error);
        throw error;
    }
};

// 사용자 프로필 정보 가져오기
export const getNicknameProfile = async (nickname: string) => {
    try {
        const response = await axios.get(API_QUERY_NICKNAME_PROFILE_URL(nickname));
        console.log("Nickname profile fetched successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching nickname profile:", error);
        throw error;
    }
};

// 제목으로 사용자 게시글 검색
export const searchNicknamePostsByTitle = async (
    nickname: string,
    title: string,
    page: number = 0,
    size: number = 20
) => {
    try {
        const response = await axios.get(API_QUERY_NICKNAME_SEARCH_POSTS_BY_TITLE_URL(nickname), {
            params: { title, page, size },
        });
        console.log("Nickname's posts searched by title successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error searching nickname's posts by title:", error);
        throw error;
    }
};

// 내용으로 사용자 게시글 검색
export const searchNicknamePostsByContent = async (
    nickname: string,
    content: string,
    page: number = 0,
    size: number = 20
) => {
    try {
        const response = await axios.get(API_QUERY_NICKNAME_SEARCH_POSTS_BY_CONTENT_URL(nickname), {
            params: { content, page, size },
        });
        console.log("Nickname's posts searched by content successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error searching nickname's posts by content:", error);
        throw error;
    }
};

// 텍스트로 사용자 게시글 검색
export const searchNicknamePostsByText = async (
    nickname: string,
    text: string,
    page: number = 0,
    size: number = 20
) => {
    try {
        const response = await axios.get(API_QUERY_NICKNAME_SEARCH_POSTS_BY_TEXT_URL(), {
            params: { nickname, text, page, size },
        });
        console.log("Nickname's posts searched by text successfully:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error searching nickname's posts by text:", error);
        throw error;
    }
};