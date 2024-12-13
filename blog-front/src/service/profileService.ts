import axios from "axios";
import { API_PROFILE_CREATE_OR_UPDATE_URL } from "../util/apiUrl";

export type ProfileRequestDTO = {
    nickname: string;
    email: string;
    title: string;
    intro: string;
}

export const createOrUpdateProfile = async (requestDTO: ProfileRequestDTO, token: string) => {
    try {
        const response = await axios.post(
            API_PROFILE_CREATE_OR_UPDATE_URL(),
            requestDTO,
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            }
        );

        console.log('Profile update successful:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error update post:', error);
        throw error;
    }
}