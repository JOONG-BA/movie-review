import RatingBar from "@/components/moive/detail/RatingBar.jsx";


export const MovieDetailInfo = ({ movie }) => {
    return (
        <div className="bg-gray-50 py-10">
            <div className="container flex flex-row m-auto">
                {/*포스터*/}
                <div className="w-full max-w-[280px] md:max-w-[240px] lg:max-w-[280px]">
                    <div className="w-full h-[380px] bg-gray-300">
                    </div>
                </div>
                {/*영화상세정보*/}
                <div className="w-full flex ml-8 flex-1 flex-col">
                    <section className="w-full">
                        <RatingBar movieId={movie.id}></RatingBar>
                    </section>
                    <section className="w-full border-t border-t-gray-300">
                        <div className="text-left text-xs mt-5 text-gray-500">
                            {movie.overview}
                        </div>
                    </section>
                </div>
            </div>
        </div>
    )
}