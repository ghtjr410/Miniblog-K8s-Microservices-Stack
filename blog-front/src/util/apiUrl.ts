// common
export const API_VERSION = `/api/v1`;
export const SERVICE_ROUTES = {
    IMAGE: '/image-service',
    POST: '/post-service',
    COMMENT: '/comment-service',
    LIKE: '/like-service',
    PROFILE: '/profile-service',
    VIEWCOUNT: '/viewcount-service',
    ACCOUNT: '/account-service',
    QUERY: '/query-service',
  };

// Dev
// export const HTTP = `http://`;
// export const LOCALHOST = 'localhost:';
// export const PORT_NUMBER = {
//     KEYCLOAK: '8181',
//     BLOG: '3000',
//     API_GATEWAY: '4040'
// }

// export const KEYCLOAK_URL = `${HTTP}${LOCALHOST}${PORT_NUMBER.KEYCLOAK}/`;
// export const BLOG_URL = `${HTTP}${LOCALHOST}${PORT_NUMBER.BLOG}`;
// export const API_GATEWAY_URL = `${HTTP}${LOCALHOST}${PORT_NUMBER.API_GATEWAY}`;

// Prod
export const HTTPS = `https://`;
export const ROOT_DOMAIN = `.ghtjr.com`;
export const SUB_DOMAINS = {
    KEYCLOAK: 'keycloak',
    BLOG: 'blog',
    API_GATEWAY: 'api'
}
export const KEYCLOAK_URL = `${HTTPS}${SUB_DOMAINS.KEYCLOAK}${ROOT_DOMAIN}/`;
export const BLOG_URL = `${HTTPS}${SUB_DOMAINS.BLOG}${ROOT_DOMAIN}`;
export const API_GATEWAY_URL = `${HTTPS}${SUB_DOMAINS.API_GATEWAY}${ROOT_DOMAIN}`;
 

// Image Service
export const CLOUD_FRONT_URL = `https://images.ghtjr.com/`;
export const API_IMAGE_URL = `${API_GATEWAY_URL}${API_VERSION}${SERVICE_ROUTES.IMAGE}`;
export const API_IMAGE_PRESIGNED_URL = `${API_IMAGE_URL}/images/presigned-url`;

// Post Service
export const API_POST_URL = `${API_GATEWAY_URL}${API_VERSION}${SERVICE_ROUTES.POST}`;

export const API_POST_CREATE_URL = () => `${API_POST_URL}/posts`;
export const API_POST_UPDATE_URL = (postUuid: string) =>`${API_POST_URL}/posts/${postUuid}`;
export const API_POST_DELETE_URL = (postUuid: string) =>`${API_POST_URL}/posts/${postUuid}`;

// Comment Service
export const API_COMMENT_URL = `${API_GATEWAY_URL}${API_VERSION}${SERVICE_ROUTES.COMMENT}`;

export const API_COMMENT_CREATE_URL = () => `${API_COMMENT_URL}/comments`;
export const API_COMMENT_UPDATE_URL = (commentUuid: string) => `${API_COMMENT_URL}/comments/${commentUuid}`;
export const API_COMMENT_DELETE_URL = (commentUuid: string) => `${API_COMMENT_URL}/comments/${commentUuid}`;

// Like Service
export const API_LIKE_URL = `${API_GATEWAY_URL}${API_VERSION}${SERVICE_ROUTES.LIKE}`;

export const API_LIKE_TOGGLE_URL = (postUuid: string) => `${API_LIKE_URL}/posts/${postUuid}/likes/toggle`;

// Profile Service
export const API_PROFILE_URL = `${API_GATEWAY_URL}${API_VERSION}${SERVICE_ROUTES.PROFILE}`

export const API_PROFILE_CREATE_OR_UPDATE_URL = () =>`${API_PROFILE_URL}/profiles`

// Viewcount Service
export const API_VIEWCOUNT_URL = `${API_GATEWAY_URL}${API_VERSION}${SERVICE_ROUTES.VIEWCOUNT}`

export const API_VIEWCOUNT_INCREMENT_URL = (postUuid: string) => `${API_VIEWCOUNT_URL}/posts/${postUuid}/total-views/increment`

// Account Service
export const API_ACCOUNT_URL = `${API_GATEWAY_URL}${API_VERSION}${SERVICE_ROUTES.ACCOUNT}`

export const API_ACCOUNT_DELETE_URL = () => `${API_ACCOUNT_URL}/user`;

// Query Service
export const API_QUERY_URL = `${API_GATEWAY_URL}${API_VERSION}${SERVICE_ROUTES.QUERY}`

// 비로그인 + 로그인 
// 전체 게시글 최신순으로 40개씩 가져오기
export const API_QUERY_LATEST_POSTS_URL = () => `${API_QUERY_URL}/posts/latest`
// 전체 게시글 좋아요순으로 40개씩 가져오기
export const API_QUERY_MOST_LIKED_POSTS_URL = () => `${API_QUERY_URL}/posts/most-liked`
// 전체 게시글 조회수순으로 40개씩 가져오기
export const API_QUERY_MOST_VIEWED_POSTS_URL = () => `${API_QUERY_URL}/posts/most-viewed`;
// 전체 범위 제목으로 검색하기
export const API_QUERY_SEARCH_POSTS_BY_TITLE_URL = () => `${API_QUERY_URL}/posts/search/title`;
// 전체 범위 내용으로 검색하기
export const API_QUERY_SEARCH_POSTS_BY_CONTENT_URL = () => `${API_QUERY_URL}/posts/search/content`;
// 전체 범위 텍스트 검색
export const API_QUERY_SEARCH_POSTS_BY_TEXT_URL = () => `${API_QUERY_URL}/posts/search`;
// 게시글 UUID로 해당 게시글과 연관된 댓글 가져오기
export const API_QUERY_POST_DETAIL_URL = (postUuid: string) => `${API_QUERY_URL}/posts/${postUuid}`;

// 로그인 
// 내가 좋아요 누른 게시글 목록 가져오기
export const API_QUERY_USER_LIKED_POSTS_URL = (userUuid: string) => `${API_QUERY_URL}/user/${userUuid}/liked-posts`;
// 내가 작성한 댓글 목록 가져오기
export const API_QUERY_USER_COMMENTED_POSTS_URL = (userUuid: string) => `${API_QUERY_URL}/user/${userUuid}/commented-posts`;
// 다른유저가 작성한 게시글 전체 목록 가져오기
export const API_QUERY_NICKNAME_POSTS_URL = (nickname: string) => `${API_QUERY_URL}/user/${nickname}/posts`;
// 다른유저가 작성한 게시글 최신순으로 20개씩 가져오기
export const API_QUERY_NICKNAME_LATEST_POSTS_URL = (nickname: string) => `${API_QUERY_URL}/user/${nickname}/posts/latest`;
// 다른유저가 작성한 게시글 좋아요순으로 20개씩 가져오기
export const API_QUERY_NICKNAME_MOST_LIKED_POSTS_URL = (nickname: string) => `${API_QUERY_URL}/user/${nickname}/posts/most-liked`;
// 다른유저가 작성한 게시글 조회수순으로 20개씩 가져오기
export const API_QUERY_NICKNAME_MOST_VIEWED_POSTS_URL = (nickname: string) => `${API_QUERY_URL}/user/${nickname}/posts/most-viewed`;
// 다른유저가 UUID로 프로필 정보 가져오기
export const API_QUERY_NICKNAME_PROFILE_URL = (nickname: string) => `${API_QUERY_URL}/user/${nickname}/profile`;
// 다른유저가 UUID로 제목으로 게시글 검색하기
export const API_QUERY_NICKNAME_SEARCH_POSTS_BY_TITLE_URL = (nickname: string) => `${API_QUERY_URL}/user/${nickname}/posts/search/title`;
// 다른유저가 UUID로 텍스트로 게시글 검색하기
export const API_QUERY_NICKNAME_SEARCH_POSTS_BY_TEXT_URL = () => `${API_QUERY_URL}/user/posts/search`;
// 다른유저가 UUID로 내용으로 게시글 검색하기
export const API_QUERY_NICKNAME_SEARCH_POSTS_BY_CONTENT_URL = (nickname: string) => `${API_QUERY_URL}/user/${nickname}/posts/search/content`;