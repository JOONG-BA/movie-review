import React from "react";

export default function NotFound({
        title = "페이지를 찾을 수 없습니다",
        description = "요청하신 페이지가 존재하지 않거나 삭제되었습니다.",
        emoji = "🔍"
    }) {
    return (
        <div className="flex flex-col items-center justify-center text-center pb-10 h-full px-4 text-gray-600 h-dvh">
            <div className="text-3xl mb-4">{emoji}</div>
            <h2 className="text-xl font-bold mb-2">{title}</h2>
            <p className="text-sm text-gray-400">{description}</p>
        </div>
    );
}