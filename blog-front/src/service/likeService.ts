import axios from "axios";
import { API_LIKE_TOGGLE_URL } from "../util/apiUrl";

export const toggleLike = async (postUuid: string, token: string) => {
    try {
        const response = await axios.post(
            API_LIKE_TOGGLE_URL(postUuid),
            {},
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            }
        );

        console.log('Like toggle successful:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error toggle like:', error);
        throw error;
    }
}