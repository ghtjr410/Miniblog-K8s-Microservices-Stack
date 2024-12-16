export const ROUTES = {
    HOME: '/',
    SETTING: '/setting',
    POST_WRITE: '/write',
    POST: '/post',
    SEARCH: '/search',
    BLOG: '/blog',
    HISTORY: '/history',
    PROFILE: '/profile',
    TEST: '/test',
};

export const DYNAMIC_ROUTES = {
    POST_DETAIL: (nickname: string = ":nickname", postUuid: string = ":postUuid") => `/user/${nickname}/posts/${postUuid}`,
    POST_REWRITE: (postUuid: string = ":id") => `/write?id=${postUuid}`,
    USER_BLOG: (nickname: string = ":nickname") => `/${nickname}/posts`
}

