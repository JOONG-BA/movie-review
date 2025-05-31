import React from "react";

// 더미 데이터
const favoriteMovies = [
  { id: 1, title: "기억의 조각들", posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg" },
  { id: 2, title: "마지막 여름", posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg" },
  { id: 3, title: "초능력자들", posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg" },
  { id: 4, title: "코미디의 왕", posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg" },
];

const reviewedMovies = [
  { id: 5, title: "공포의 밤", posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg", myRating: 4.2 },
  { id: 6, title: "드라마틱 인생", posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg", myRating: 4.7 },
  { id: 7, title: "사랑과 전쟁", posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg", myRating: 4.5 },
  { id: 8, title: "하늘의 별", posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg", myRating: 3.9 },
];

const MyPage = () => {
  return (
    <div className="min-h-screen bg-gray-100 flex justify-center pt-10 px-4">
      <div className="bg-white rounded-lg shadow-md max-w-3xl w-full">
        {/* 프로필 상단 */}
        <div className="flex items-center gap-6 p-6 border-b border-gray-200 relative">
          <div className="p-2 rounded-full bg-gray-100 border border-gray-300 flex-shrink-0 w-28 h-28 overflow-hidden">
            <img
              src="https://i.namu.wiki/i/m1WHrelfgKjmdgckinSKZApCLjRnRvMVoJFtsyJ_ahL21yTZMZxChJW0gG01uh2JzljEHYhvmzdhxCqQ_lhPv61XV-GaEVZhJvILmJpHC2s2E2sKbdrF21sznEoFwdbwFoC9CQVosHGQKurnt7Atig.webp"
              alt="프로필 이미지"
              className="w-full h-full object-cover rounded-full"
            />
          </div>

          <div>
            <h2 className="text-2xl font-semibold mb-1 text-gray-900">이산</h2>
            <p className="text-sm text-gray-600 mb-2">san4013@naver.com</p>
            <div className="flex text-sm text-gray-600 gap-6">
              <div>
                팔로워 <span className="font-semibold text-black">0</span>
              </div>
              <div>
                팔로잉 <span className="font-semibold text-black">0</span>
              </div>
            </div>
          </div>

          <button
            aria-label="설정"
            title="설정"
            className="absolute top-4 right-4 p-2 hover:bg-gray-100 rounded-full"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-6 w-6 text-gray-700"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              strokeWidth={2}
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M12 9v3m0 0v3m0-3h3m-3 0H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z"
              />
            </svg>
          </button>
        </div>

        {/* 하단 평가, 코멘트, 컬렉션 */}
        <div className="flex justify-evenly text-center text-gray-700 text-sm p-6 gap-x-10">
          <div>
            <div className="text-lg font-semibold text-gray-900 mb-2">12</div>
            <div className="mt-2">평가</div>
          </div>
          <div className="border-l border-r border-gray-300 px-10">
            <div className="text-lg font-semibold text-gray-900 mb-2">0</div>
            <div className="mt-2">코멘트</div>
          </div>
          <div>
            <div className="text-lg font-semibold text-gray-900 mb-2">0</div>
            <div className="mt-2">컬렉션</div>
          </div>
        </div>

        {/* 즐겨찾기 영화 */}
        <div className="px-6 mb-10">
          <h3 className="text-xl font-bold text-gray-900 mb-4">⭐ 최근 즐겨찾기한 영화</h3>
          <div className="grid grid-cols-3 gap-4">
            {favoriteMovies.slice(0, 3).map((movie) => (
              <div key={movie.id} className="rounded overflow-hidden shadow-md">
                <img src={movie.posterUrl} alt={movie.title} className="w-full h-64 object-cover" />
                <div className="p-2 text-sm text-center font-medium">{movie.title}</div>
              </div>
            ))}
          </div>
        </div>

        {/* 내가 높게 평가한 영화 */}
        <div className="px-6 mb-10">
          <h3 className="text-xl font-bold text-gray-900 mb-4">🔥 내가 높게 평가한 영화</h3>
          <div className="grid grid-cols-3 gap-4">
            {reviewedMovies.sort((a, b) => b.myRating - a.myRating).slice(0, 3).map((movie) => (
              <div key={movie.id} className="rounded overflow-hidden shadow-md">
                <img src={movie.posterUrl} alt={movie.title} className="w-full h-64 object-cover" />
                <div className="p-2 text-sm text-center font-medium">{movie.title}</div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default MyPage;
