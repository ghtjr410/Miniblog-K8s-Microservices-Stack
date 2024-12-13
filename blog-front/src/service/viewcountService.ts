import axios from "axios";
import { API_VIEWCOUNT_INCREMENT_URL } from "../util/apiUrl";

export const incrementViewcount = async (postUuid: string) => {
    try {
        const response = await axios.post(
            API_VIEWCOUNT_INCREMENT_URL(postUuid),
        );

        console.log('Viewcount update successful:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error update viewcount:', error);
        throw error;
    }
}