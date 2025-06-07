// src/api/favoriteApi.js
import { authFetch } from "@/context/AuthContext";

// 즐겨찾기 추가
export async function addFavorite(movieId) {
    const res = await authFetch(`/api/users/me/favorites/${movieId}`, {
        method: "POST",
    });

    console.log(res);


    if (!res.ok) {
        let errorMessage;
        try {
            errorMessage = await res.text(); // JSON이 아닐 수도 있음
        } catch (e) {
            errorMessage = "서버로부터 응답을 읽을 수 없습니다.";
        }
        throw new Error(`(${res.status}) ${errorMessage || "요청이 거부되었습니다."}`);
    }

    const data = await res.json();
    return data;
}

// 즐겨찾기 삭제
export async function removeFavorite(movieId) {
    const res = await authFetch(`/api/users/me/favorites/${movieId}`, {
        method: "DELETE",
    });

    console.log(res);

    if (!res.ok) {
        const error = await res.json();
        throw new Error(error.message || "즐겨찾기 해제 실패");
    }

    return res.json();
}
