import axios from "axios";

import { API_ACCOUNT_DELETE_URL } from "../util/apiUrl";

export const deleteAccount = async (token: string) => {
    try {
        const response = await axios.delete(
            API_ACCOUNT_DELETE_URL(),
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            }
        )

        console.log('Delete Account Successful:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error delete account:', error);
        throw error;
    }
}