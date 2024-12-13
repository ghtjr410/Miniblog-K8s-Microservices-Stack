import axios from "axios";
import { API_POST_CREATE_URL, API_POST_DELETE_URL, API_POST_UPDATE_URL } from "../util/apiUrl";

export type PostCreateRequestDTO = {
    nickname: string;
    title: string;
    content: string;
}

export const createPost = async (requestDTO: PostCreateRequestDTO, token: string) => {
    try {
        const response = await axios.post(
            API_POST_CREATE_URL(),
            requestDTO,
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            }
        );

        console.log('Post creation successful:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error creating post:', error);
        throw error;
    }
};

export type PostUpdateRequestDTO = {
    title: string;
    content: string;
}

export const updatePost = async (requestDTO: PostUpdateRequestDTO, postUuid: string, token: string) => {
    try {
        const response = await axios.put(
            API_POST_UPDATE_URL(postUuid),
            requestDTO,
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            }
        );
        console.log('Post update successful:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error updating post:', error);
        throw error;
    }
}

export const deletePost = async (postUuid: string, token: string) => {
    try {
        const response = await axios.delete(
            API_POST_DELETE_URL(postUuid),
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            }
        );
        console.log('Post deletion successful:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error deleting post:', error);
        throw error;
    }
}