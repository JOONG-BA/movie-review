import {useEffect, useRef, useState} from "react";
import {NextArrow} from "@/components/slider/PrevArrow.jsx";
import {PrevArrow} from "@/components/slider/NextArrow.jsx";
import Slider from "react-slick";

export default function MovieVideos({ videos = [] }) {
    const [selectedImage, setSelectedImage] = useState(null);
    const [isDragging, setIsDragging] = useState(false);

    const sliderRef = useRef();

    useEffect(() => {
        if (sliderRef.current) {
            sliderRef.current.slickGoTo(0); // 강제 초기 정렬
        }
    }, []);

    const settings = {
        slidesToShow: 3,
        slidesToScroll: 3,
        initialSlide: 0,
        speed: 1000,
        arrows: true,
        infinite: false,
        nextArrow: <NextArrow />,
        prevArrow: <PrevArrow />,
        swipe: false,
        responsive: [
            {
                breakpoint: 768,
                settings: {
                    centerMode: true,
                    slidesToShow: 1,
                    slidesToScroll: 1,
                    swipe: true,
                    arrows: false,
                    infinite: true,
                    centerPadding: "15%",
                }
            }
        ],
    };
    if (videos.length === 0) return null;

    return (
        <div className="mt-8 container">
            <h2 className="text-xl text-left font-bold my-4">동영상</h2>

            {videos.length === 1 ? (
                // 단일 영상일 경우
                <a
                    href={videos[0].url}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="block max-w-lg relative rounded overflow-hidden"
                >
                    <img
                        src={`https://img.youtube.com/vi/${new URL(videos[0].url).searchParams.get("v")}/0.jpg`}
                        alt={videos[0].name}
                        className="aspect-video object-cover w-full rounded hover:opacity-80"
                    />
                    <div className="mt-2 text-sm text-left text-gray-800 font-medium truncate">
                        {videos[0].name}
                    </div>
                </a>
            ) : (
                // 2개 이상일 경우 슬라이더
                <Slider ref={sliderRef} {...settings} className="relative">
                    {videos.map((video, idx) => {
                        const videoId = new URL(video.url).searchParams.get("v");
                        return (
                            <div className="px-1" key={idx}>
                                <a
                                    href={video.url}
                                    target="_blank"
                                    rel="noopener noreferrer"
                                    className="block relative rounded overflow-hidden"
                                >
                                    <img
                                        src={`https://img.youtube.com/vi/${videoId}/0.jpg`}
                                        alt={video.name}
                                        className="aspect-video object-cover w-full rounded hover:opacity-80"
                                    />
                                    <div className="mt-2 text-sm text-left text-gray-800 font-medium truncate">
                                        {video.name}
                                    </div>
                                </a>
                            </div>
                        );
                    })}
                </Slider>
            )}
        </div>
    );
}

