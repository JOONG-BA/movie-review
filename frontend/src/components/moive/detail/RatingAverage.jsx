import { FaStar } from 'react-icons/fa';

export default function RatingAverage({ average }) {
    return (
        <div className="flex flex-1 flex-col space-y-2 justify-end items-center text-sm">
          <span className="text-4xl font-bold text-gray-700">
                { average }
          </span>
            <span className="text-xs">평균평점</span>
        </div>
    );
}
