import { ChevronLeft, ChevronRight } from 'lucide-react';
import proptypes from 'prop-types';

function Pagination({ currentPage, totalPages, onPageChange }) {
    return (
        <div className="flex items-center justify-center space-x-2 mt-4">
            <button
                onClick={() => onPageChange(currentPage - 1)}
                disabled={currentPage === 1}
                className="p-2 rounded-md hover:bg-gray-100 disabled:opacity-50 disabled:cursor-not-allowed"
            >
                <ChevronLeft className="h-5 w-5 text-gray-600" />
            </button>

            <span className="text-sm text-gray-600">
                Page {currentPage} of {totalPages}
            </span>

            <button
                onClick={() => onPageChange(currentPage + 1)}
                disabled={currentPage === totalPages}
                className="p-2 rounded-md hover:bg-gray-100 disabled:opacity-50 disabled:cursor-not-allowed"
            >
                <ChevronRight className="h-5 w-5 text-gray-600" />
            </button>
        </div>
    );
};

Pagination.propTypes = {
    currentPage: proptypes.number.isRequired,
    totalPages: proptypes.number.isRequired,
    onPageChange: proptypes.func.isRequired,
};

export default Pagination;