import {useEffect, useRef, useState} from "react";
import Slider from "react-slick";
import {NextArrow} from "@/components/slider/PrevArrow.jsx";
import {PrevArrow} from "@/components/slider/NextArrow.jsx";

export default function MovieGallery({ images = [] }) {
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
    };

    const handleImageClick = (src) => {
        if (!isDragging) {
            setSelectedImage(src);
        }
    };

    return (
        <div className="mt-8 container">
            <h2 className="text-xl text-left font-bold my-4">갤러리</h2>
            <Slider ref={sliderRef}  {...settings} className="relative" >
                {images.map((src, idx) => (
                    <div className="px-1">
                        <img
                            key={idx}
                            src={src}
                            alt={`gallery-${idx}`}
                            onClick={() => handleImageClick(src)}
                            className="cursor-pointer object-cover aspect-[3/2] rounded"
                        />
                    </div>
                ))}
            </Slider>

            {selectedImage && (
                <div
                    className="fixed inset-0 z-50 bg-black/80 flex items-center justify-center focus:border-0"
                    onClick={() => setSelectedImage(null)}
                >
                    <img
                        src={selectedImage}
                        alt="Selected"
                        className="max-w-[90vw] max-h-[90vh] rounded shadow-lg"
                        onClick={(e) => e.stopPropagation()} // 이미지 클릭 시 닫히지 않도록
                    />
                </div>
            )}
        </div>
    );
}
