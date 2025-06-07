// src/pages/MyPage.jsx
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function MyPage() {
  const navigate = useNavigate();

  /** ---------------- state ---------------- */
  const [profile, setProfile] = useState(null);           // MyPageDTO 통째로
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  /** ------------ helpers --------------- */
  const fetchMyPage = async () => {
    try {
      const token = window.localStorage.getItem("token");
      if (!token) throw new Error("로그인이 필요합니다.");

      /* (1) 마이페이지 DTO 요청 */
      const res = await fetch("http://localhost:8080/api/users/me", {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
      if (!res.ok) throw new Error(`status ${res.status}`);
      const dto = await res.json();

      /* (2) 최근 리뷰 3편을 위한 포스터·제목 채우기 (선택) */
      const reviewsWithPoster = await Promise.all(
          dto.recentReviews.map(async (rv) => {
            try {
              const mvRes = await fetch(
                  `http://localhost:8080/api/movies/${rv.movieId}`
              );
              const mv = await mvRes.json();
              return { ...rv, posterPath: mv.posterPath, title: mv.title };
            } catch {
              return rv; // 실패하면 포스터 없이라도 표시
            }
          })
      );

      setProfile({ ...dto, recentReviews: reviewsWithPoster });
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  };

  /** ------------- effect --------------- */
  useEffect(() => {
    fetchMyPage();
  }, []);

  /** ------------- UI ------------------- */
  if (loading) return <div className="p-8 text-center">Loading…</div>;
  if (error)
    return (
        <div className="p-8 text-center text-red-600">
          {error}{" "}
          <button
              onClick={() => navigate("/login")}
              className="underline font-medium"
          >
            로그인 페이지로
          </button>
        </div>
    );
  if (!profile) return null;

  return (
      <div className="min-h-screen bg-gray-100 flex justify-center pt-10 px-4">
        <div className="bg-white rounded-lg shadow-md max-w-3xl w-full">
          {/* 프로필 상단 */}
          <div className="flex items-center gap-6 p-6 border-b border-gray-200 relative">
            <div className="p-2 rounded-full bg-gray-100 border border-gray-300 flex-shrink-0 w-28 h-28 overflow-hidden">
              {/* 사용자 프로필 이미지는 backend URL을 저장해두셨다면 교체 */}
              <img
                  src="https://i.pinimg.com/736x/2f/55/97/2f559707c3b04a1964b37856f00ad608.jpg"
                  alt="프로필"
                  className="w-full h-full object-cover rounded-full"
              />
            </div>

            <div>
              <h2 className="text-2xl font-semibold mb-1 text-gray-900">
                {profile.nickname}
              </h2>
              <p className="text-sm text-gray-600 mb-2">{profile.email}</p>
            </div>
          </div>

          {/* 평가·코멘트·즐겨찾기 수 */}
          <div className="flex justify-evenly text-center text-gray-700 text-sm p-6 gap-x-10">
            <Stat label="평가" value={profile.reviewCount} />
            <div className="border-l border-r border-gray-300 px-10">
              <Stat label="코멘트" value={profile.recentReviews.length} />
            </div>
            <Stat label="즐겨찾기" value={profile.favoriteCount} />
          </div>

          {/* 즐겨찾기 Section */}
          <Section title="최근 즐겨찾기한 영화">
            <MovieGrid movies={profile.favoriteMovies.slice(0, 3)} />
          </Section>

          {/* 리뷰 Section */}
          <Section title="최근 리뷰 영화">
            <MovieGrid
                movies={profile.recentReviews
                    .sort((a, b) => b.score - a.score)
                    .slice(0, 3)}
            />
          </Section>
        </div>
      </div>
  );
}

/* ---------- 재사용 가능한 작은 컴포넌트 ---------- */
const Stat = ({ label, value }) => (
    <div>
      <div className="text-lg font-semibold text-gray-900 mb-2">{value}</div>
      <div className="mt-2">{label}</div>
    </div>
);

const Section = ({ title, children }) => (
    <div className="px-6 mb-10">
      <h3 className="text-xl font-bold text-gray-900 mb-4">{title}</h3>
      {children}
    </div>
);

const MovieGrid = ({ movies }) => (
    <div className="grid grid-cols-3 gap-4">
      {movies.map((m) => (
          <div key={m.movieId || m.id} className="rounded overflow-hidden shadow-md">
            <img
                src={m.posterPath || m.posterUrl}
                alt={m.title}
                className="w-full h-64 object-cover"
            />
            <div className="p-2 text-sm text-center font-medium">{m.title}</div>
          </div>
      ))}
    </div>
);
