package net.imagej;

import net.imglib2.FinalRealInterval;
import net.imglib2.RealPoint;
import net.imglib2.RealRandomAccessibleRealInterval;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.realtransform.AffineRealRandomAccessible;
import net.imglib2.roi.Regions;
import net.imglib2.roi.boundary.Boundary;
import net.imglib2.roi.geometric.GeomRegions;
import net.imglib2.roi.geometric.HyperRectangle;
import net.imglib2.roi.geometric.Polygon2D;
import net.imglib2.roi.geometric.RasterizedRegion;
import net.imglib2.roi.operators.RealBinaryOperator;
import net.imglib2.type.logic.BoolType;

public class BoundaryExamples {

	private BoundaryExamples() {
		// prevent instantiation of utility class
	}

	public static ImageJ launch(final String... args) {
		final ImageJ ij = new ImageJ();
		ij.launch(args);

		return ij;
	}

	public static void main(final String... args) {
		final ImageJ ij = launch(args);

//		final RasterizedRegion<HyperRectangle, BoolType> r = Regions.rasterize(GeomRegions.hyperRectangle(new RealPoint(new double[] {100, 50}), new double[] {100, 50}));
//		final Boundary<BoolType> b = new Boundary<>(r, Boundary.StructuringElement.FOUR_CONNECTED);
//
//		ij.ui().show(r);
//		ij.ui().show(b);

//		final RasterizedRegion<Polygon2D, BoolType> p = Regions.rasterize(PolygonTests.createSelfIntersectingPolygon());
//		final Boundary<BoolType> b4 = new Boundary<>(p, Boundary.StructuringElement.FOUR_CONNECTED);
//		final Boundary<BoolType> b8 = new Boundary<>(p, Boundary.StructuringElement.EIGHT_CONNECTED);
//
//		ij.ui().show(p);
//		ij.ui().show(b4);
//		ij.ui().show(b8);

//		final double angle = 45.0 * (Math.PI / 180.0);
//		final double[][] matrix = new double[][] { { Math.cos(angle), -Math.sin(
//			angle) }, { Math.sin(angle), Math.cos(angle) } };
//		final AffineRealRandomAccessible<BoolType, AffineGet> rotatedHE =
//			GeomRegions.hyperEllipsoid(new RealPoint(new double[] { 150, 100 }),
//				new double[] { 50, 100 }, matrix);
//		// Still need to add an interval to rotated shapes because the combination
//		// methods only work on RealRandomAccessibleRealIntervals
//		final RealRandomAccessibleRealInterval<BoolType> rotatedHEInterval = Regions
//			.interval(rotatedHE, new FinalRealInterval(new double[] { 0, 0 },
//				new double[] { 250, 200 }));
//
//		final HyperRectangle hr = GeomRegions.hyperRectangle(new RealPoint(
//			new double[] { 210, 50 }), new double[] { 100, 45 });
//
//		final RealBinaryOperator<BoolType> or = Regions.union(
//			rotatedHEInterval, hr);
//		final RasterizedRegion<RealBinaryOperator<BoolType>, BoolType> ror =
//			Regions.rasterize(or);
//
//		final Boundary<BoolType> b = new Boundary<>(ror, Boundary.StructuringElement.EIGHT_CONNECTED);
//
//		ij.ui().show(ror);
//		ij.ui().show(b);

//		final Boundary<BoolType> b = new Boundary<>(OtherGeomTests.createPointCollection());
//
//		ij.ui().show(OtherGeomTests.createPointCollection());
//		ij.ui().show(b);
	}
}
