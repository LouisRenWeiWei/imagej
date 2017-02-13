
package net.imagej;

import java.util.ArrayList;
import java.util.List;

import net.imglib2.RealInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.RealRandomAccessibleRealInterval;
import net.imglib2.img.Img;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.realtransform.AffineRealRandomAccessible;
import net.imglib2.realtransform.AffineTransform;
import net.imglib2.roi.Regions;
import net.imglib2.roi.geometric.HyperEllipsoid;
import net.imglib2.roi.geometric.RasterizedRegion;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.logic.BoolType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

public class HyperEllipsoidExamples {

	private HyperEllipsoidExamples() {
		// prevent instantiation of utility class
	}

	public static ImageJ launch(final String... args) {
		final ImageJ ij = new ImageJ();
		ij.launch(args);

		return ij;
	}

	public static void main(final String... args) {
		final ImageJ ij = launch(args);

		final HyperEllipsoid ellipse = HyperEllipsoidTests.createHyperEllipsoid();
		final HyperEllipsoid ellipse3 = HyperEllipsoidTests
			.create3DHyperEllipsoid();
		final AffineRealRandomAccessible<BoolType, AffineGet> rotated =
			HyperEllipsoidTests.createRotated3DHyperEllipsoidDisplay();

		final RasterizedRegion<HyperEllipsoid, BoolType> rasterEllipse = Regions
			.rasterize(ellipse);
		final RasterizedRegion<HyperEllipsoid, BoolType> rasterEllipse3 = Regions
			.rasterize(ellipse3);

		// Add interval to rotated ellipse then rasterize
		final HyperEllipsoid sourceRotated = (HyperEllipsoid) rotated.getSource();
		final double[] center = new double[sourceRotated.numDimensions()];
		sourceRotated.localize(center);
		final double[] semiaxes = new double[] { sourceRotated.getSemiAxisLength(0),
			sourceRotated.getSemiAxisLength(1), sourceRotated.getSemiAxisLength(2) };
		final RealInterval interval = getCorners(semiaxes, center,
			(AffineTransform) rotated.getTransformToSource().inverse());

		final RealRandomAccessibleRealInterval<BoolType> rotatedInterval = Regions
			.interval(rotated, interval);
		final RasterizedRegion<RealRandomAccessibleRealInterval<BoolType>, BoolType> rasterRotated =
			Regions.rasterize(rotatedInterval);

		ij.ui().show(rasterEllipse);

		// NB: transform RAI into ImgPlus for displaying
		final IntervalView<BoolType> in = Views.interval(rasterEllipse3,
			rasterEllipse3);
		final Img<DoubleType> i = ij.op().create().img(new int[] { 41, 81, 101 });
		final Img<DoubleType> d = ij.op().convert().float64(in);
		ij.op().copy().img(i, d);
		final ImgPlus<DoubleType> ip = ij.op().create().imgPlus(i);

		ij.ui().show(ip);

		final IntervalView<BoolType> itr = Views.interval(rasterRotated,
			rasterRotated);
		final Img<DoubleType> img = ij.op().create().img(new int[] { 41, 123,
			111 });
		final Img<DoubleType> conv = ij.op().convert().float64(itr);
		ij.op().copy().img(img, conv);
		final Img<BitType> b = ij.op().convert().bit(img);
		final ImgPlus<BitType> imgplus = ij.op().create().imgPlus(b);

		ij.ui().show(imgplus);
	}

	// -- Helper methods --

	private static RealInterval getCorners(final double[] semiAxes,
		final double[] center, final AffineTransform transform)
	{
		final int dim = center.length;
		final List<RealLocalizable> corners = new ArrayList<>();
		final double numCorners = Math.pow(2, dim);

		for (int p = 0; p < numCorners; p++) {
			corners.add(new RealPoint(dim));
		}

		double change = numCorners / 2;
		for (int n = 0; n < dim; n++) {
			double axis = semiAxes[n];
			for (int p = 0; p < numCorners; p++) {
				if (p % change == 0) axis = -axis;
				final RealPoint vert = (RealPoint) corners.get(p);
				vert.setPosition(center[n] + axis, n);
			}
			change = change / 2;
		}

		for (int p = 0; p < numCorners; p++) {
			final RealPoint vert = (RealPoint) corners.get(p);
			final double[] source = new double[dim];
			final double[] target = new double[dim];

			vert.localize(source);
			transform.apply(source, target);
			vert.setPosition(target);
		}

		return Regions.getBoundsReal(corners);
	}
}
