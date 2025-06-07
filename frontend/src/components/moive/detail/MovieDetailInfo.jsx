import RatingBar from "@/components/moive/detail/RatingBar.jsx";

export const MovieDetailInfo = ({ movie }) => {
  return (
      <div className="bg-gray-50 pt-12 pb-16">
        <div className="container flex flex-col-reverse sm:flex-row m-auto">

          {/* 왼쪽 포스터 */}
          <div className="w-full m-auto max-w-[280px] md:max-w-[240px] lg:max-w-[280px]">
            <img src={movie.poster_path} alt={movie.title} />
          </div>

          {/* 오른쪽 영화 상세 정보 */}
          <div className="w-full flex ml-0 sm:ml-8 flex-1 flex-col">
            <section className="w-full mb-2">
              {/* movieId 전달 추가 */}
              <RatingBar voteAverage={movie.voteAverage} movieId={movie.id} isFavorite={movie.favorite}/>
            </section>

            <section className="w-full mb-10 sm:mb-0 border-t border-t-gray-300">
              <div className="text-left text-xs mt-6 text-gray-500">
                {movie.overview}
              </div>
            </section>
          </div>
        </div>
      </div>
  );
};
