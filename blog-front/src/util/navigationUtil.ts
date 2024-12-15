import { useNavigate } from "react-router-dom"
import { DYNAMIC_ROUTES, ROUTES } from "../constants/routes";


const useNavigationHelper = () => {
    const navigate = useNavigate();
    // 정적
    const toHome = () => navigate(ROUTES.HOME);
    const toSetting = () => navigate(ROUTES.SETTING);
    const toPostWrite = () => navigate(ROUTES.POST_WRITE);
    
    // 동적
    const toPostDetail = (nickname: string, postUuid: string) => {
        navigate(DYNAMIC_ROUTES.POST_DETAIL(nickname, postUuid));
    }
    const toPostRewrite = (postUuid: string) => {
        navigate(DYNAMIC_ROUTES.POST_REWRITE(postUuid));
    }
    const toUserBlog = (nickname: string) => {
        navigate(DYNAMIC_ROUTES.USER_BLOG(nickname));
    }

    return { toHome, toPostWrite, toSetting, toPostDetail, toPostRewrite, toUserBlog }
}

export default useNavigationHelper;