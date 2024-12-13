import axios from "axios";
import { API_COMMENT_CREATE_URL, API_COMMENT_DELETE_URL, API_COMMENT_UPDATE_URL } from "../util/apiUrl";

export type CommetCreateRequestDTO = {
    postUuid: string;
    nickname: string;
    content: string;
}

export const createComment = async (requestDTO: CommetCreateRequestDTO, token: string) => {
    try {
        const response = await axios.post(
            API_COMMENT_CREATE_URL(),
            requestDTO,
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            }
        );

        console.log('Comment creation successful:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error creating comment:', error);
        throw error;
    }
}

export type CommentUpdateRequestDTO = {
    content: string;
}

export const updateComment = async (requestDTO: CommentUpdateRequestDTO, commentUuid: string, token: string) => {
    try {
        const response = await axios.put(
            API_COMMENT_UPDATE_URL(commentUuid),
            requestDTO,
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            }
        );
        console.log('Comment update successful:', response.data);
        return response.data;
    } catch (error) {
        console.log('Comment update successful:', error);
        throw error;
    }
}

export const deleteComment = async (commentUuid: string, token: string) => {
    try {
        const response = await axios.delete(
            API_COMMENT_DELETE_URL(commentUuid),
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            }
        );
        console.log('Comment deletion successful:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error deleting comment:', error);
        throw error;
    }
}