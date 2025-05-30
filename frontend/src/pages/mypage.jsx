import React from "react";

const MyPage = () => {
  return (
    <div className="min-h-screen bg-gray-100 flex justify-center pt-10 px-4">
      <div className="bg-white rounded-lg shadow-md max-w-3xl w-full">
        {/* 프로필 상단 박스 */}
        <div className="flex items-center gap-6 p-6 border-b border-gray-200 relative">
          {/* 프로필 이미지 */}
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
      </div>
    </div>
  );
};

export default MyPage;
