// src/pages/Login.jsx
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Login() {
    const navigate = useNavigate();

    // 폼 상태
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setLoading(true);

        try {
            const res = await fetch("http://localhost:8080/api/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password }),
            });

            if (!res.ok) {
                // 백엔드가 {"error":"메시지"} 형태로 주는 경우 처리
                const { error } = await res.json();
                throw new Error(error || `status ${res.status}`);
            }

            const { token } = await res.json();
            // ① 토큰 저장 ― MyPage.jsx에서 읽어 가는 이름과 맞춰 줌
            localStorage.setItem("token", token);

            // ② 마이페이지로 이동
            navigate("/mypage");
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-100">
            <form
                onSubmit={handleSubmit}
                className="bg-white shadow-md rounded-lg p-8 w-80"
            >
                <h1 className="text-2xl font-bold text-center mb-6">로그인</h1>

                {error && (
                    <p className="text-red-600 text-sm mb-4 text-center">{error}</p>
                )}

                <label className="block mb-4">
                    <span className="text-gray-700 text-sm">이메일</span>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className="mt-1 block w-full px-3 py-2 border rounded-md focus:outline-none focus:ring"
                        required
                    />
                </label>

                <label className="block mb-6">
                    <span className="text-gray-700 text-sm">비밀번호</span>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="mt-1 block w-full px-3 py-2 border rounded-md focus:outline-none focus:ring"
                        required
                    />
                </label>

                <button
                    type="submit"
                    disabled={loading}
                    className="w-full bg-indigo-600 text-white py-2 rounded-md hover:bg-indigo-700 transition disabled:opacity-50"
                >
                    {loading ? "로그인 중…" : "로그인"}
                </button>
            </form>
        </div>
    );
}
