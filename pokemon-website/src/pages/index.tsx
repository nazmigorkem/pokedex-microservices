import InfiniteScrollPokelist from '#/components/main/view/InfiniteScrollPokelist';
import { SERVER_URL, fetchForInfiniteScroll } from '#/endpoints/Fetcher';
import { POKEMON_SERVER_ENDPOINTS } from '#/endpoints/Pokemon';
import { useRouter } from 'next/router';
import { useState } from 'react';

export default function Home() {
	const [hasMore, setHasMore] = useState(true);
	const [pageNumber, setPageNumber] = useState(0);
	const [items, setItems] = useState([] as any[]);
	const [searchValue, setSearchValue] = useState('');
	const router = useRouter();
	const fetchPokemons = fetchForInfiniteScroll(
		`${SERVER_URL}${POKEMON_SERVER_ENDPOINTS.SEARCH}`,
		pageNumber,
		items,
		searchValue,
		setHasMore,
		setPageNumber,
		setItems
	);

	return (
		<div className="flex flex-col items-center mt-20">
			<div className="flex gap-3">
				<input
					onChange={(e) => {
						setSearchValue(e.target.value);
					}}
					type="text"
					className="input input-accent input-bordered"
					placeholder="Search for a pokemon"
				/>
				<button
					onClick={() => {
						router.push(`/pokemon/search/${searchValue}`);
					}}
					className="btn btn-outline btn-accent"
				>
					<i className="fas fa-search"></i>
				</button>
			</div>
			<InfiniteScrollPokelist items={items} fetchFunction={fetchPokemons} hasMore={hasMore} />
		</div>
	);
}
