import {authFetch} from "@/context/AuthContext.jsx";

export async function addFavorite(movieId) {
    const res = await authFetch(`/api/users/me/favorites/${movieId}`, {
        method: "POST",
    });
    if (!res.ok) {
        const result = await res.json();
        throw new Error(result.error || "추가 실패");
    }
}

export async function deleteFavorite(movieId) {
    const res = await authFetch(`/api/users/me/favorites/${movieId}`, {
        method: "DELETE",
    });
    if (!res.ok) {
        const result = await res.json();
        throw new Error(result.error || "삭제 실패");
    }
}

export async function checkIsFavorite(movieId) {
    const res = await authFetch(`/api/users/me/favorites/${movieId}/exists`);
    if (!res.ok) {
        // 응답이 JSON이 아닐 수도 있으므로 방어적으로 처리
        let message = "즐겨찾기 여부 확인 실패";
        try {
            const result = await res.json();
            message = result.error || message;
        } catch (_) {
            if (res.status === 403) message = "접근 권한이 없습니다 (로그인이 필요합니다)";
            if (res.status === 401) message = "로그인이 필요합니다";
        }
        throw new Error(message);
    }

    const result = await res.json();
    return result.isFavorite;
}

// 내 즐겨찾기 목록 전체 불러오기
export async function fetchMyFavorites() {
    const res = await authFetch("/api/users/me/favorites");

    const data = await res.json();

    if (!res.ok) {
        throw new Error(data.error || "즐겨찾기 목록을 불러오는 데 실패했습니다.");
    }

    return data; // List<FavoriteMovieDTO>
}