
package net.imagej;

import net.imglib2.FinalRealInterval;
import net.imglib2.RealPoint;
import net.imglib2.RealRandomAccessibleRealInterval;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.realtransform.AffineRealRandomAccessible;
import net.imglib2.roi.Regions;
import net.imglib2.roi.geometric.GeomRegions;
import net.imglib2.roi.geometric.HyperEllipsoid;
import net.imglib2.roi.geometric.HyperRectangle;
import net.imglib2.roi.geometric.Line;
import net.imglib2.roi.geometric.Polygon2D;
import net.imglib2.roi.geometric.RasterizedRegion;
import net.imglib2.roi.operators.RealBinaryOperator;
import net.imglib2.roi.operators.RealUnaryNot;
import net.imglib2.type.logic.BoolType;

public class CombinedRegionExamples {

	private CombinedRegionExamples() {
		// prevent instantiation of utility class
	}

	public static ImageJ launch(final String... args) {
		final ImageJ ij = new ImageJ();
		ij.launch(args);

		return ij;
	}

	public static void main(final String... args) {
		final ImageJ ij = launch(args);

		not(ij);
		and(ij);
		xor(ij);
		or(ij);
		subtract(ij);
		combineFour(ij);
		combineLine(ij);
	}

	private static void not(final ImageJ ij) {
		final HyperEllipsoid circle = GeomRegions.hyperEllipsoid(new RealPoint(
			new double[] { 20, 20 }), new double[] { 20, 20 });

		final RealRandomAccessibleRealInterval<BoolType> not = Regions.not(circle);
		final RasterizedRegion<RealRandomAccessibleRealInterval<BoolType>, BoolType> rnot =
			Regions.rasterize(not);

		// Can retrieve the input
		final HyperEllipsoid circle2 = (HyperEllipsoid) ((RealUnaryNot<BoolType>) not).getSource();

		ij.ui().show(rnot);
	}

	private static void xor(final ImageJ ij) {
		final HyperRectangle hr = GeomRegions.hyperRectangle(new RealPoint(
			new double[] { 100, 150, 50 }), new double[] { 100, 150, 50 });
		final HyperEllipsoid he = GeomRegions.hyperEllipsoid(new RealPoint(
			new double[] { 100, 150, 50 }), new double[] { 80, 100, 48 });

		final RealBinaryOperator<BoolType> xor = Regions.xor(hr, he);
		final RasterizedRegion<RealBinaryOperator<BoolType>, BoolType> rxor =
			Regions.rasterize(xor);

		// Can retrieve both inputs to xor
		final HyperRectangle hr2 = (HyperRectangle) xor.getA();
		final HyperEllipsoid he2 = (HyperEllipsoid) xor.getB();

		ij.ui().show(rxor);
	}

	private static void and(final ImageJ ij) {
		final Polygon2D hexagon = PolygonTests.createHexagon();
		final HyperRectangle square = GeomRegions.hyperRectangle(new RealPoint(
			new double[] { 75, 50 }), new double[] { 40, 40 });

		final RealBinaryOperator<BoolType> and = Regions.intersect(
			hexagon, square);
		final RasterizedRegion<RealBinaryOperator<BoolType>, BoolType> rand =
			Regions.rasterize(and);

		ij.ui().show(rand);
	}

	private static void or(final ImageJ ij) {
		final double angle = 45.0 * (Math.PI / 180.0);
		final double[][] matrix = new double[][] { { Math.cos(angle), -Math.sin(
			angle) }, { Math.sin(angle), Math.cos(angle) } };
		final AffineRealRandomAccessible<BoolType, AffineGet> rotatedHE =
			GeomRegions.hyperEllipsoid(new RealPoint(new double[] { 150, 100 }),
				new double[] { 50, 100 }, matrix);
		// Still need to add an interval to rotated shapes because the combination
		// methods only work on RealRandomAccessibleRealIntervals
		final RealRandomAccessibleRealInterval<BoolType> rotatedHEInterval = Regions
			.interval(rotatedHE, new FinalRealInterval(new double[] { 0, 0 },
				new double[] { 250, 200 }));

		final HyperRectangle hr = GeomRegions.hyperRectangle(new RealPoint(
			new double[] { 210, 50 }), new double[] { 100, 45 });

		final RealBinaryOperator<BoolType> or = Regions.union(
			rotatedHEInterval, hr);
		final RasterizedRegion<RealBinaryOperator<BoolType>, BoolType> ror =
			Regions.rasterize(or);

		ij.ui().show(ror);
	}

	private static void subtract(final ImageJ ij) {
		final Polygon2D selfIntersect = PolygonTests
			.createSelfIntersectingPolygon();
		final HyperRectangle hr = GeomRegions.hyperRectangle(new RealPoint(
			new double[] { 50, 60 }), new double[] { 30, 70 });

		final RealBinaryOperator<BoolType> subtract = Regions
			.subtract(selfIntersect, hr);
		final RasterizedRegion<RealBinaryOperator<BoolType>, BoolType> rsubtract =
			Regions.rasterize(subtract);

		ij.ui().show(rsubtract);
	}

	private static void combineFour(final ImageJ ij) {
		final double angle = 45.0 * (Math.PI / 180.0);
		final double[][] matrix = new double[][] { { Math.cos(angle), -Math.sin(
			angle) }, { Math.sin(angle), Math.cos(angle) } };
		final AffineRealRandomAccessible<BoolType, AffineGet> rotatedHE =
			GeomRegions.hyperEllipsoid(new RealPoint(new double[] { 150, 100 }),
				new double[] { 50, 100 }, matrix);
		// Still need to add an interval to rotated shapes because the combination
		// methods only work on RealRandomAccessibleRealIntervals
		final RealRandomAccessibleRealInterval<BoolType> rotatedHEInterval = Regions
			.interval(rotatedHE, new FinalRealInterval(new double[] { 0, 0 },
				new double[] { 250, 200 }));

		final HyperRectangle hr = GeomRegions.hyperRectangle(new RealPoint(
			new double[] { 100, 100 }), new double[] { 10, 50 });
		final RealBinaryOperator<BoolType> xor = Regions.xor(hr,
			rotatedHEInterval);

		final HyperRectangle square = GeomRegions.hyperRectangle(new RealPoint(
			new double[] { 70, 70 }), new double[] { 30, 30 });
		final RealBinaryOperator<BoolType> subtract = Regions
			.subtract(xor, square);

		final Polygon2D hexagon = PolygonTests.createHexagon();
		final RealBinaryOperator<BoolType> or = Regions.union(hexagon,
			subtract);

		final RasterizedRegion<RealBinaryOperator<BoolType>, BoolType> rCombine =
			Regions.rasterize(or);

		ij.ui().show(rCombine);
	}

	private static void combineLine(final ImageJ ij) {
		final Line line = OtherGeomTests.createLine();
		final HyperEllipsoid circle = GeomRegions.hyperEllipsoid(new RealPoint(
			new double[] { 30, 30 }), new double[] { 20, 20 });

		final RealBinaryOperator<BoolType> combine = Regions.union(
			line, circle);
		final RasterizedRegion<RealBinaryOperator<BoolType>, BoolType> rCombine =
			Regions.rasterize(combine);

		ij.ui().show(rCombine);
	}

	/*
	 * Regions.xor(), union(), intersect(), subtract(), and not() will not work
	 * with RealPoints and PointCollections. 
	 * 
	 * It doesn't work with PointCollections because PointCollection is integer
	 * space and all the operators are for real space. Should we create integer
	 * space combination methods? i.e.
	 * 
	 * BinaryOperator xor(RandomAccessibleInterval<B> A,
	 * RandomAccessibleInterval<B> B) {
	 * 		return new BinaryXOr(A, B);
	 * }
	 * 
	 * You can't combine RealPoints using these methods because they're not
	 * Intervals. Should we create combine methods which operate on RealPoints,
	 * transform a RealPoint into a zero width/height interval, or just not allow
	 * points to be combined?
	 */

	/*
	 * The below doesn't work because hexagon does not have the same
	 * dimensionality as the other two. In order for this to work, we'd need to
	 * be able to embed the 2D hexagon in 3D.
	 */

//	private static void combineMultiple(final ImageJ ij) {
//		final HyperRectangle hr = GeomRegions.hyperRectangle(new RealPoint(
//			new double[] { 100, 150, 50 }), new double[] { 100, 150, 50 });
//		final HyperEllipsoid he = GeomRegions.hyperEllipsoid(new RealPoint(
//			new double[] { 100, 150, 50 }), new double[] { 80, 100, 48 });
//		final Polygon2D hexagon = PolygonTests.createHexagon();
//
//		final RealRandomAccessibleRealInterval<BoolType> xor = Regions.xor(hr, he);
//		final RealRandomAccessibleRealInterval<BoolType> combineThree = Regions
//			.subtract(xor, hexagon);
//		final RasterizedRegion<RealRandomAccessibleRealInterval<BoolType>, BoolType> rCombineThree =
//			Regions.rasterize(combineThree);
//
//		ij.ui().show(rCombineThree);
//	}
}
