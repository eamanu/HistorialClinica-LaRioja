export const FLAVORED_FOOTER_IMAGES: FlavoredImagesObj = {
	tandil: [
		{
			location: 'assets/flavors/tandil/images/logos/ministerio-salud.png',
			alt: 'ministerio-salud'
		},
		{
			location: 'assets/flavors/tandil/images/logos/sisp.png',
			alt: 'sisp'
		},
		{
			location: 'assets/flavors/tandil/images/logos/minicipio-tandil.png',
			alt: 'minicipio-tandil'
		},
		{
			location: 'assets/flavors/tandil/images/logos/pladema.png',
			alt: 'pladema'
		},
		{
			location: 'assets/flavors/tandil/images/logos/unicen.png',
			alt: 'unicen'
		},
	],
	minsal: [
		{
			location: 'assets/logos/marca_larioja.png',
			alt: 'larioja'
		},
	],
	pba: [
		{
			location: 'assets/flavors/pba/images/logos/ministerio-salud.png',
			alt: 'ministerio-salud'
		},
		{
			location: 'assets/flavors/pba/images/logos/pladema.png',
			alt: 'pladema'
		},
		{
			location: 'assets/flavors/pba/images/logos/unicen.png',
			alt: 'unicen'
		},
	],
	chaco: [
		{
			location: 'assets/flavors/chaco/images/logos/ministerio-salud.png',
			alt: 'ministerio-salud'
		},
		{
			location: 'assets/flavors/chaco/images/logos/prov-chaco.png',
			alt: 'prov-chaco'
		},
		{
			location: 'assets/flavors/chaco/images/logos/ministerio-salud-chaco.png',
			alt: 'ministerio-salud-chaco'
		},
		{
			location: 'assets/flavors/chaco/images/logos/pladema.png',
			alt: 'pladema'
		},
		{
			location: 'assets/flavors/chaco/images/logos/unicen.png',
			alt: 'unicen'
		},
	],
};

export interface ImageSrc {
	location: string;
	alt: string;
}

export interface FlavoredImagesObj {
	[flavor: string]: ImageSrc[];
}
