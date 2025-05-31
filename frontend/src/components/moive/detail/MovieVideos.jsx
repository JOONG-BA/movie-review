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
        infinite: true,
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
                    centerPadding: "15%",
                }
            }
        ],
        beforeChange: () => setIsDragging(true),
        afterChange: () => setTimeout(() => setIsDragging(false), 50)
    };

    const handleImageClick = (src) => {
        if (!isDragging) {
            setSelectedImage(src);
        }
    };

    return (
        <div className="mt-8 container">
            <h2 className="text-xl text-left font-bold my-4">동영상</h2>
            <Slider ref={sliderRef}  {...settings} className="relative" >
                {videos.map((video, idx) => (
                    <div className="px-1">
                        <a
                            key={idx}
                            href={`https://www.youtube.com/watch?v=${video.key}`}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="block relative rounded overflow-hidden"
                        >
                            <img
                                src={`https://img.youtube.com/vi/${video.key}/0.jpg`}
                                alt={video.name}
                                className="aspect-video object-cover w-full rounded hover:opacity-80"
                            />
                            <div className="mt-2 text-sm  text-left text-gray-800 font-medium truncate">
                                {video.name}
                            </div>
                        </a>
                    </div>
                ))}
            </Slider>
        </div>
    );
}
