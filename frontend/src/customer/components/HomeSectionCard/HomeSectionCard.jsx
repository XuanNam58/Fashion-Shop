import React from 'react';
import { useNavigate } from 'react-router-dom';

const HomeSectionCard = ({product}) => {
    const navigate = useNavigate();
    const handleClick = () => {
        navigate(`/product/${product.id}`);
    }
    return (
        <div className='cursor-pointer flex flex-col items-center 
        bg-white rounded-lg shadow-lg overflow-hidden w-[15rem] mx-3 border'
        onClick={handleClick}
        >

            <div className='h-[13rem] w-[10rem]'>
                <img className='object-cover object-top w-full h-full' 
                src={product.imageUrl} 
                alt='product' />
            </div>

            <div className='p-4'>
                <h3 className='text-lg font-medium text-gray-900'>{product.brand}</h3>
                <p className='mt-2 text-sm text-gray-500'>{product.title}</p>
            </div>
        </div>
    )
}

export default HomeSectionCard;